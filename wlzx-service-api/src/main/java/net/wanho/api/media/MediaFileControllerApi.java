package net.wanho.api.media;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.wanho.common.vo.response.AjaxResult;
import net.wano.po.media.request.QueryMediaFileRequest;

/**
 * Created by Administrator.
 */
@Api(value = "媒体文件管理",tags = {"媒体文件管理接口"})
public interface MediaFileControllerApi {

    @ApiOperation("我的媒资文件查询列表")
    public AjaxResult findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest);

}

