package net.wanho.aouth_jwt.config;

import net.wanho.aouth_jwt.service.WlzxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * 拦截e(AuthenticationManagerBuilder auth)，安全管理器
 */
@Configuration
//开启web安全器
@EnableWebSecurity
//对全部方法进行验证，全局的方法验证（--方法的权限授权，）
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private WlzxUserService wlzxUserService;

    /**
     * 配置内存登录用户验证
     *
     * @param auth
     * @throws Exception
     */

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 自己写页面去匹配 安全
        //用户从业务层获取
        auth.userDetailsService(wlzxUserService);
}


}
