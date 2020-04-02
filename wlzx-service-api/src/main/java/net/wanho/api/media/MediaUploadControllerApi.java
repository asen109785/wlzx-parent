package net.wanho.api.media;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.wanho.common.vo.response.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Administrator.
 */
@Api(value = "媒资管理接口",description = "媒资管理接口，提供文件上传、处理等接口")
public interface MediaUploadControllerApi {

    //文件上传前的准备工作,校验文件是否存在
    @ApiOperation("文件上传注册")
    public AjaxResult register(String fileMd5,
                               String fileName,
                               Long fileSize,
                               String mimetype,
                               String fileExt);

    @ApiOperation("校验分块文件是否存在")
    public AjaxResult checkchunk(String fileMd5,
                                 Integer chunk,
                                 Integer chunkSize);

    @ApiOperation("上传分块")
    public AjaxResult uploadchunk(MultipartFile file,
                                  String fileMd5,
                                  Integer chunk);

    @ApiOperation("合并分块")
    public AjaxResult mergechunks(String fileMd5,
                                  String fileName,
                                  Long fileSize,
                                  String mimetype,
                                  String fileExt);

}
