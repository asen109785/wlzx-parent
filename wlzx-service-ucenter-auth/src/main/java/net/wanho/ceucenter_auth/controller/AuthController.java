package net.wanho.ceucenter_auth.controller;

import net.wanho.api.auth.AuthControllerApi;
import net.wanho.ceucenter_auth.service.AuthService;
import net.wanho.common.util.CookieUtil;
import net.wanho.common.util.ServletUtils;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.web.BaseController;
import net.wano.po.ucenter.ext.AuthToken;
import net.wano.po.ucenter.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class AuthController extends BaseController implements AuthControllerApi {

    @Autowired
    private AuthService authService;

    @Value("${auth.clientId}")
    private String clinetId;
    @Value("${auth.clientSecret}")
    private String clientSecret;
    @Value("${auth.cookieDomain}")
    private String cookieDomain;
    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;


    /**
     *
     * @param loginRequest
     * @return
     */
    @Override
    @PostMapping("/userlogin")
    public AjaxResult login(LoginRequest loginRequest) {
        AuthToken authToken = authService.login(loginRequest.getUsername(),loginRequest.getPassword(),clinetId,clientSecret);
        // jtl访问短令牌保存到cookie
        this.saveJtiToCookie(authToken.getJti_token());

        //获得访问的令牌token
        String token = authToken.getJti_token();
        return success("登录成功",token);
    }

    private void saveJtiToCookie(String jti_token) {
        HttpServletResponse response = ServletUtils.getResponse();
//        响应+cookie域名 + cookie名字 + 内容 + 有效期 + 是否只再http中使用
        CookieUtil.addCookie(response,cookieDomain,"/","uid",jti_token,cookieMaxAge,false);
    }

    /**
     * 退出
     * @return
     */
    @Override
    @PostMapping("/userlogout")
    public AjaxResult logout() {
        //删除cookie
        String tokenFormCookie = getTokenFormCookie();
        HttpServletResponse response = ServletUtils.getResponse();
        CookieUtil.addCookie(response,cookieDomain,"/","uid",tokenFormCookie,0,false);
        //删除redis
        boolean result = authService.delToken(tokenFormCookie);

        return success();
    }


    /**
     * 登录获取用户信息
     * @return
     */
    @Override
    @GetMapping("/userjwt")
    public AjaxResult userjwt() {
        //取出cookie中的用户身份令牌.短令牌
        String jti_token = getTokenFormCookie();
        if(jti_token == null){
            return fail();
        }
        //从redis获得用户jwt令牌
        //拿身份令牌从redis中查询jwt令牌
        AuthToken userToken = authService.getUserTokenFormRedis(jti_token);
        if(userToken!=null){
            //将jwt令牌返回给用户
            String jwt_token = userToken.getAccess_token();
            return success("",jwt_token);
        }
        return null;
    }

    //取出cookie中的用户身份令牌.短令牌
    private String getTokenFormCookie() {
        HttpServletRequest request = ServletUtils.getRequest();
        Map<String, String> map = CookieUtil.readCookie(request, "uid");
        if(map!=null && map.get("uid")!=null){
            String uid = map.get("uid");
            return uid;
        }
        return null;
    }
}
