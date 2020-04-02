package net.wanho.ceucenter_auth.config;


import net.wanho.ceucenter_auth.service.WlzxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


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
    private WlzxUserService wlzxUserService;

    // 密码模式授权 认证管理
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

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

    //Http安全配置，对每个到达系统的http请求链接进行校验
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //所有请求必须认证通过
        http.csrf().disable()
                .httpBasic().and()
                .formLogin()
                .permitAll()  //表单登录，permitAll()表示这个不需要验证 登录页面，登录失败页面
                .and()
                .authorizeRequests()
                .antMatchers("/login","/oauth/token","/userlogin","/userjwt","/userlogout","/v2/api‐docs", "/swagger‐resources/configuration/ui",
                        "/swagger‐resources","/swagger‐resources/configuration/security",
                        "/swagger‐ui.html","/webjars/**","/course/coursePic/list/**","/course/courseView/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
    }
}
