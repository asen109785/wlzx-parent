package net.wanho.ceucenter_auth.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.wanho.common.exception.ExceptionCast;
import net.wano.po.ucenter.ext.AuthToken;
import net.wano.po.ucenter.request.LoginRequest;
import net.wano.po.ucenter.response.AuthCode;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuthService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${auth.tokenValiditySeconds}")
    private int tokenValiditySeconds;


    // 获得密码授权模式的令牌
    public AuthToken login(String username, String password, String clinetId, String clientSecret) {
        //申请一个令牌
        AuthToken authToken = this.applyToken(username, password, clinetId, clientSecret);
        if (StringUtils.isEmpty(authToken)){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }
        //访问令牌
        String access_token = authToken.getAccess_token();
        //段令牌 jti jwt令牌
        String jti = authToken.getJti_token();


        // access_token访问令牌保存到redis缓存中
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            boolean saveResult = this.saveTokenToRedis(jti, objectMapper.writeValueAsString(authToken), tokenValiditySeconds);
            if (!saveResult){
                ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);
            }
        } catch (JsonProcessingException e) {
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);
            log.error(e.getMessage());
        }

        return authToken;
    }

//   access_token访问令牌保存到redis缓存中
    private boolean saveTokenToRedis(String jti, String token, int tokenValiditySeconds) {
        String key = "user_token:"+jti;
        stringRedisTemplate.opsForValue().set(key,token,tokenValiditySeconds,TimeUnit.SECONDS);
        return stringRedisTemplate.getExpire(key)>0;
    }

    private AuthToken applyToken(String username, String password, String client_id, String client_secret) {
        // 授权方式
        String grant_type = "password";

        //获取令牌信息的请求地址
        String url = "http://localhost:40400/oauth/token";
        //存放Authrization =>  Basic Auth,
        LinkedMultiValueMap<String,String> header = new LinkedMultiValueMap<>();
        String httpBasic = getHttpBasic(client_id,client_secret);
        header.add("Authorization",httpBasic);
        // RestTemplate 发起http请求
        // exchange(url + 请求类型 + 请求参数 + 接收类型)
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type",grant_type);
        body.add("username",username);
        body.add("password",password);
        HttpEntity<MultiValueMap<String,String>> httpEitity = new HttpEntity<>(body,header);
        ResponseEntity<Map> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEitity, Map.class);
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 400){
                ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
            }else if (e.getRawStatusCode() == 401) {
                ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
            }
            return null;
        }
        Map map = responseEntity.getBody();

        //        获得各种令牌
        String access_token = (String) map.get("access_token");
        String refresh_token = (String) map.get("refresh_token");
        String jti = (String) map.get("jti");
        //验证非空
        if (StringUtils.isEmpty(map)||StringUtils.isEmpty(refresh_token)||StringUtils.isEmpty(access_token)||StringUtils.isEmpty(jti)){
            ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
        }
        AuthToken authToken = new AuthToken();
        authToken.setAccess_token(access_token);
        authToken.setRefresh_token(refresh_token);
        authToken.setJti_token(jti);
        return authToken;
    }

    /**
     * client_id:client_secret  需要加密
     * @param client_id
     * @param client_secret
     * @return
     */
    private String getHttpBasic(String client_id, String client_secret) {
        String str = client_id+":"+client_secret;
        return "Basic "+ new String(Base64Utils.encode(str.getBytes()));

    }

    public AuthToken getUserTokenFormRedis(String jti_token) {
        String key = "user_token:" + jti_token;
        //从redis中取到令牌信息
        String value = stringRedisTemplate.opsForValue().get(key);
        //转成对象

        try {
            AuthToken authToken = JSON.parseObject(value, AuthToken.class);
            return authToken;
        } catch (Exception e) {
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_ERROR);
            log.error(e.getMessage());
        }
        return null;
    }

    // 删除redis令牌
    public boolean delToken(String tokenFormCookie) {
        String name = "user_token:" + tokenFormCookie;
        stringRedisTemplate.delete(name);
        return true;
    }
}
