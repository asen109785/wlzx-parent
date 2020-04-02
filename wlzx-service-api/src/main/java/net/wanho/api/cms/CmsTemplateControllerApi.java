package net.wanho.api.cms;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.wanho.common.vo.response.AjaxResult;

@Api(value="cms模板管理接口",tags = "cms模板管理接口")
public interface CmsTemplateControllerApi {

    //查询站点信息
    @ApiOperation("查询模板信息")
    public AjaxResult findAllCmsTemplate(String siteId);
}
