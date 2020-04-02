package net.wanho.aouth_jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * 认证服务
 * 需要添加 @EnableAuthorizationServer注解开启认证服务，注入加密用的BCryptPasswordEncoder实例。然后，配置需要认证的客户端，
 * 这里需要细说一下，首先是client_id代表是哪个客户端也就是哪个APP或者web服务需要认证的，然后是客户端的secret秘钥需要加密，
 * authorizedGrantTypes授权方式指的是授权码，简单，客户端，账户密码等，这里使用的是授权码（authorization_code），然后是scopes范围，
 * redirectUris重定向地址，就是你的登录地址，授权后跳转的地址。
 */
@Configuration
@EnableAuthorizationServer
public class AuthConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 从内存中
        clients.inMemory()
                .withClient("client")   //哪个服务需要认证
                .secret(bCryptPasswordEncoder.encode("secret")) //服务客户端 secret秘钥
                .authorizedGrantTypes("authorization_code")  //授权类型，授权码
                .scopes("app")   //范围
                .redirectUris("http://localhost:9000/login");//拦截 重定向
    }

}