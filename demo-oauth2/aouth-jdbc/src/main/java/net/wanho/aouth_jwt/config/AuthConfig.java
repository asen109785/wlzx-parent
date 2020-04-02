package net.wanho.aouth_jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;


@Configuration
@EnableAuthorizationServer
public class AuthConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    /**
     * TokenStore 的默认实现有三种：
     * InMemoryTokenStore ： 默认
     * JdbcTokenStore
     * JwtTokenStore
     * @return
     */

    public TokenStore tokenStore(){
//        return new InMemoryTokenStore(); //默认 内存令牌
        return  new JdbcTokenStore(dataSource);
    };

    //只需配置这个bean即可 但是我们的datasource是在yml配置文件中配置好了的,只需要注入:

    public ClientDetailsService clientDetailsService(){
        return  new JdbcClientDetailsService(dataSource);
    };

//
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore());
    }

    /**
     * 配置用户登录验证服务
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        通过clientDetailsService 的数据源去查找 数据库固定表的 内容，客户端id  秘钥 方式什么的
        clients.withClientDetails(clientDetailsService());
    }

}