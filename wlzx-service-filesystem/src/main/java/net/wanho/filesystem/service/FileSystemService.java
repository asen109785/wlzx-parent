package net.wanho.filesystem.service;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import net.wanho.common.exception.ExceptionCast;
import net.wanho.common.util.StringUtils;
import net.wanho.filesystem.mapper.FileSystemMapper;
import net.wano.po.filesystem.FileSystem;
import net.wano.po.filesystem.response.FileSystemCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileSystemService {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Resource
    private FileSystemMapper fileSystemMapper;

    public StorePath upload(MultipartFile multipartFile, String fileTag, String businessKey, String metadata) {

        //文件验证
        if(StringUtils.isEmpty(multipartFile)){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }

        InputStream is = null;
        try {
            is = multipartFile.getInputStream();
//            文件全名
            String filename = multipartFile.getOriginalFilename();
//            文件扩展名
            String suffix = filename.substring(filename.lastIndexOf(".")+1);
            //第一步:将文件上传到fastDFS中，得到一个StorePath
            StorePath storePath = fastFileStorageClient.uploadFile(is, is.available(), suffix, null);

            //第二步：将文件id及其它文件信息存储到mysql中
            FileSystem fileSystem = new FileSystem();
            //fileSystem.setFileId(storePath.getFullPath());
            fileSystem.setFilePath(storePath.getFullPath());
            fileSystem.setFileSize(is.available());
            fileSystem.setFileName(filename);
            fileSystem.setFileType(multipartFile.getContentType());
            fileSystem.setFileTag(fileTag);
            fileSystem.setBusinessKey(businessKey);
            //todo 登录功能实现之后完善
            //fileSystem.setUserId("");
            fileSystemMapper.insert(fileSystem);
            return storePath;
        } catch (IOException e) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
            return null;
        }

    }
}
