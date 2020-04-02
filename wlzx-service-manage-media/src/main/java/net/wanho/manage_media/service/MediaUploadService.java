package net.wanho.manage_media.service;

import lombok.extern.slf4j.Slf4j;
import net.wanho.common.exception.ExceptionCast;
import net.wanho.common.util.StringUtils;
import net.wanho.manage_media.mapper.MediaFileMapper;
import net.wano.po.media.MediaFile;
import net.wano.po.media.response.MediaCode;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.Date;

@Slf4j
@Service
public class MediaUploadService {

    @Autowired
    private MediaFileService mediaFileService;

    @Resource
    private MediaFileMapper mediaFileMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${wlzx.upload-location}")
    private String upload_location ;

    @Value("${wlzx.mq.exchange}")
    private String exchange_name;
    @Value("${wlzx.mq.routingKey}")
    private String routing_key;

    /**
     * 上传前检查上传环境
     * - 检查文件是否上传，已上传则直接返回。
     * - 检查文件上传路径是否存在，不存在则创建。
     * - 根据主键(md5值)检查文件信息在mysql中是否存在
     *
     * 根据文件md5得到文件路径
     * 规则：
     *      * 一级目录：md5的第一个字符
     *      * 二级目录：md5的第二个字符
     *      * 三级目录：md5
     *      * 文件名：md5+文件扩展名
     * @param fileMd5 文件md5值
     * @param fileName 文件名
     * @param fileSize 文件大小
     * @param mimetype
     * @param fileExt 文件扩展名
     */
    public void register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        //1  检查文件在磁盘上是否存在
        //文件目录的路径
        String fileFolderPath= this.getFileFolderPath(fileMd5);
        //文件路径
        String filePath= this.getFilePath(fileMd5,fileExt);

        File file = new File(filePath);
        if(file.exists()){
            //文件存在
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }

        // 检查文件上传路径是否存在
        File fileFolder = new File(fileFolderPath);
        if(!fileFolder.exists()){
            fileFolder.mkdirs();
        }

        //的media_file表中是否存在
        MediaFile one = mediaFileService.getById(fileMd5);
        if(StringUtils.isNotEmpty(one)){
            //文件存在
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }

    }

//    文件路径
    private String getFilePath(String fileMd5, String fileExt) {
        return getFileFolderPath(fileMd5)+fileMd5+"."+fileExt;
    }


     //文件目录的路径
    private String getFileFolderPath(String fileMd5) {
//        upload_location  媒资的绝对路径的前缀   ，一级目录+二级目录+三级目录+/
        return this.upload_location+ fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/";

    }

    /**
     * 分块检查
     * - 检查分块文件是否上传，已上传则返回true。
     * @param fileMd5
     * @param chunk
     * @param chunkSize
     * @return
     */
    public boolean checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        //块目录
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        //块文件
        File chunkFile = new File(chunkFileFolderPath+chunk);
        return chunkFile.exists();
    }

    private String getChunkFileFolderPath(String fileMd5) {
        return getFileFolderPath(fileMd5)+"chunk/";
    }

    /**
     * 分块上传
     * - 将未上传的分块文件上传到指定的路径。
     * @param file
     * @param fileMd5
     * @param chunk
     */
    public void uploadchunk(MultipartFile file, String fileMd5, Integer chunk) {
        //块目录
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        // 如果块目录不存在，创建块目录
        File chunkFileFolder = new File(chunkFileFolderPath);
        if(!chunkFileFolder.exists()){
            chunkFileFolder.mkdirs();
        }
        //块文件
        File chunkFile = new File(chunkFileFolderPath+chunk);
        InputStream is =null;
        FileOutputStream os = null;
        try {
             is = file.getInputStream();
             os = new FileOutputStream(chunkFile);
            IOUtils.copy(is,os);
        } catch (Exception e) {
            log.error(e.getMessage());
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
        }finally {
            try {
                os.close();
            } catch (IOException e) {
                log.error(e.getMessage());
                ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
            }
            try {
                is.close();
            } catch (IOException e) {
                log.error(e.getMessage());
                ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
            }

        }

    }

    /**
     * 合并分块
     * - 将所有分块文件合并为一个文件。
     * - 校验文件的md5
     * - 在数据库记录文件信息
     * - 发送消息队列
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimetype
     * @param fileExt
     */
    public void mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        //创建一个合并文件
        String filePath = this.getFilePath(fileMd5,fileExt);
        File target = new File(filePath);
        //执行合并
        target = this.merget(target, fileMd5);
        if(StringUtils.isEmpty(target)){
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
        }

        //验文件的md5
        boolean flag= this.checkMd5(target,fileMd5);
        if(flag==false){
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
        }

        //在数据库记录文件信息
        this.saveMediaFile(fileMd5,fileName,fileSize,mimetype,fileExt);

        // 向MQ发送视频处理消息(m3u8)-->发送是FileId
        this.sendProcessVideoMsg(fileMd5);

    }

    /**
     * 向MQ发送视频处理消息(m3u8)-->发送是FileId
     * @param fileId
     */
    private void sendProcessVideoMsg(String fileId) {
        MediaFile mediaFile = mediaFileMapper.selectById(fileId);
        if(StringUtils.isEmpty(mediaFile)){
            ExceptionCast.cast(MediaCode.SEND_FILE_FAIL);
        }

        //存在，向MQ发送视频处理消息
        rabbitTemplate.convertAndSend(exchange_name,routing_key,fileId);


    }

    private void saveMediaFile(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileOriginalName(fileName);
        mediaFile.setFileName(fileMd5+"."+fileExt);
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileSize(fileSize);
        mediaFile.setFileType(fileExt);
        String filePath = fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/";
        mediaFile.setFilePath(filePath);
        //状态为上传成功
        mediaFile.setFileStatus("301002");
        mediaFile.setUploadTime(new Date());

        mediaFileService.save(mediaFile);
    }

    private boolean checkMd5(File target, String fileMd5) {
        FileInputStream is = null;
        try {
             is = new FileInputStream(target);
            String md5Str = DigestUtils.md5DigestAsHex(is);
            return  fileMd5.equalsIgnoreCase(md5Str);
        } catch (IOException e) {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
            return false;
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File merget( File target, String fileMd5) {
        BufferedOutputStream bos = null;
        try {
            //合并  读多写一
             bos = new BufferedOutputStream(new FileOutputStream(target));
            //块目录
            File file = new File(this.getChunkFileFolderPath(fileMd5));
            String[] strs = file.list();
            BufferedInputStream bis;
            int len = 0;
            byte[] bs = new byte[1024 * 1024];
            for (int i = 0; i < strs.length; i++) {
                bis = new BufferedInputStream(new FileInputStream(this.getChunkFileFolderPath(fileMd5) + i));
                len = bis.read(bs);
                bos.write(bs, 0, len);
                bis.close();
            }

        }catch (Exception e){
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
        }finally {
            try {
                bos.close();
            } catch (IOException e) {
                ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
            }

        }
        return target;
    }
}
