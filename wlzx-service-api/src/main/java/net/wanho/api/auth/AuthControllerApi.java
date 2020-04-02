package net.wanho.api.auth;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.wanho.common.vo.response.AjaxResult;
import net.wano.po.ucenter.request.LoginRequest;

/**
 * Created by Administrator.
 */
@Api(value = "用户认证",description = "用户认证接口")
public interface AuthControllerApi {
    @ApiOperation("登录")
    public AjaxResult login(LoginRequest loginRequest);

    @ApiOperation("退出")
    public AjaxResult logout();

    @ApiOperation("查询用户jwt令牌")
    public AjaxResult userjwt();
}
