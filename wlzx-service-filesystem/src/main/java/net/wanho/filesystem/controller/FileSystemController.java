package net.wanho.filesystem.controller;

import net.wanho.api.filesystem.FileSystemControllerApi;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.web.BaseController;
import net.wanho.filesystem.service.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/filesystem")
public class FileSystemController extends BaseController implements FileSystemControllerApi {

    @Autowired
    private FileSystemService fileSystemService;

    @Override
    @PostMapping("/upload")
    public AjaxResult upload(MultipartFile multipartFile, String fileTag, String businessKey, String metadata) {
        return success("上传成功",fileSystemService.upload(multipartFile,fileTag,businessKey,metadata));
    }
}
