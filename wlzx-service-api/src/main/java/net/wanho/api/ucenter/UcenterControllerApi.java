package net.wanho.api.ucenter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.wano.po.ucenter.ext.WlzxUserExt;

@Api(value = "用户中心",tags = "用户中心管理")
public interface UcenterControllerApi {
    @ApiOperation("根据用户账号查询用户信息")
    public WlzxUserExt getUserext(String username);
}
