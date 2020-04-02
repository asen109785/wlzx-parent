package net.wanho.api.cms;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.wanho.common.vo.response.AjaxResult;

@Api(value="cms站点管理接口",tags = "cms站点管理接口")
public interface CmsSiteControllerApi {

    //查询站点信息
    @ApiOperation("查询站点信息")
    public AjaxResult findAllCmsSite();
}
