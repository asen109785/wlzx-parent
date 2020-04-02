package net.wanho.api.filesystem;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.wanho.common.vo.response.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "文件管理接口", tags = "文件管理接口，提供文件的增、删、改、查")
public interface FileSystemControllerApi {

    //上传文件
    @ApiOperation("上传文件接口")
    public AjaxResult upload(MultipartFile multipartFile,
                             String fileTag,
                             String businessKey,
                             String metadata);
}