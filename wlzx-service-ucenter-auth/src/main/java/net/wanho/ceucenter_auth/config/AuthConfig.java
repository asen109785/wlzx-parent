package net.wanho.ceucenter_auth.config;

import net.wanho.ceucenter_auth.converter.CustomerAuthenticationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;


import javax.sql.DataSource;
import java.security.KeyPair;


@Configuration
@EnableAuthorizationServer
//@EnableRedisHttpSession
public class AuthConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AuthenticationManager authenticationManager;

    //转换器
    @Autowired
    private CustomerAuthenticationConverter customerAuthenticationConverter;

    /**
     * TokenStore 的默认实现有三种：
     * InMemoryTokenStore ： 默认
     * JdbcTokenStore
     * JwtTokenStore
     * @return
     */

    public TokenStore tokenStore(){
//        return new InMemoryTokenStore(); //默认 内存令牌
        //使用jwt作为令牌,需要一个jwt的token转换器
        return  new JwtTokenStore(jwtAccessTokenConverter());
    }

    // 转换器
    public  JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//        签名,相当于jwt自己的秘钥, 这个签名很重要，涉及到jwt令牌的安全，硬编码写在 java中明码签名不安全，就是下面这不安全
//        jwtAccessTokenConverter.setSigningKey("123456");

/*      通过工厂获得keypair键值对，来设置jwt的签名
        使用上面的签名不安全，因此使用一个密钥key来当作签名
        -alias :键值对的key
        -keyalg : 键值对的算法 RSA 一个非对称加密算法
        -keystore : 生成的key存放的位置
        keytool -genkeypair -alias lvliang
         -keyalg RSA -keystore c:Users44321Desktopkeytoolslvliang.key*/

        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("wlzx.keystore"), "wlzxkeystore".toCharArray())
                .getKeyPair("wlzxkey","wlzxkey".toCharArray());

        //需要一个密钥对， lvliang事之前设置的-alias的key
        jwtAccessTokenConverter.setKeyPair(keyPair);
//      // 设置自定义转换，目的，扩展jwt令牌的存储数据
        DefaultAccessTokenConverter defaultAccessTokenConverter = (DefaultAccessTokenConverter) jwtAccessTokenConverter.getAccessTokenConverter();
        defaultAccessTokenConverter.setUserTokenConverter(customerAuthenticationConverter);
        return  jwtAccessTokenConverter;
    }

    /**
     * //哪些用户可以来获取token
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //用于给refresh_token使用，前四种的授权方式都会传递用户名和密码，
                // 而刷新token只有用户名，因此会调用这个里面的方法来获取密码
//                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore())
                //使用jwt需要注册一个转换器
                .tokenEnhancer(jwtAccessTokenConverter())
//                开启密码认证 模式
                .authenticationManager(authenticationManager);
    }



    //只需配置这个bean即可 但是我们的datasource是在yml配置文件中配置好了的,只需要注入:
    public ClientDetailsService clientDetailsService(){
        return  new JdbcClientDetailsService(dataSource);
    };


    /**
     * 配置用户登录验证服务
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        通过clientDetailsService 的数据源去查找 数据库固定表的 内容，客户端id  秘钥 方式什么的
        clients.withClientDetails(clientDetailsService());
    }

}