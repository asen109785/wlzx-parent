package net.wanho.api.cms;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.wano.po.cms.CmsConfig;


@Api(value="cms配置管理接口",tags = "cms配置管理接口")
public interface CmsConfigControllerApi {
    @ApiOperation("根据id查询CMS配置信息")
    public CmsConfig getmodel(String id);
}
