package net.wanho.manage_media.controller;

import net.wanho.api.media.MediaUploadControllerApi;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.web.BaseController;
import net.wanho.manage_media.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/media/upload")
public class MediaUploadController extends BaseController implements MediaUploadControllerApi {

    @Autowired
    private MediaUploadService mediaUploadService;

    /**
     * 上传前检查上传环境
     * - 检查文件是否上传，已上传则直接返回。
     * - 检查文件上传路径是否存在，不存在则创建。
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
    @Override
    @PostMapping("/register")
    public AjaxResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        mediaUploadService.register(fileMd5,fileName,fileSize,mimetype,fileExt);
        return success();
    }

    /**
     * 分块检查
     * - 检查分块文件是否上传，已上传则返回true。
     * - 未上传则检查上传路径是否存在，不存在则创建。
     * @param fileMd5
     * @param chunk
     * @param chunkSize
     * @return
     */
    @Override
    @PostMapping("/checkchunk")
    public AjaxResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        return success("核对成功",mediaUploadService.checkchunk(fileMd5,chunk,chunkSize));
    }

    /**
     * 未上传过的分块上传
     * @param file
     * @param fileMd5
     * @param chunk
     * @return
     */
    @Override
    @PostMapping("/uploadchunk")
    public AjaxResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk) {
        mediaUploadService.uploadchunk(file,fileMd5,chunk);
        return success();
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
    @Override
    @PostMapping("/mergechunks")
    public AjaxResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        mediaUploadService.mergechunks(fileMd5,fileName,fileSize,mimetype,fileExt);
        return success();
    }
}
