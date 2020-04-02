package net.wanho.ceucenter_auth.service;

import net.wanho.ceucenter_auth.client.UserClient;
import net.wanho.ceucenter_auth.po.UserJwt;
import net.wanho.common.util.StringUtils;
import net.wano.po.ucenter.WlzxMenu;
import net.wano.po.ucenter.ext.WlzxUserExt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class WlzxUserService implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        示例
//        String password = bCryptPasswordEncoder.encode("admin");
//        UserJwt userJwt = new UserJwt(s,password, AuthorityUtils.createAuthorityList("role_admin","role_user"));
//        userJwt.setId("10001");
//        userJwt.setUtype("buyer");
//        userJwt.setCompanyId("1");
//        userJwt.setName("wanho");
//        userJwt.setUserPic("pic/1.jpg");

        //获得登录用户信息
        WlzxUserExt userext = userClient.getUserext(username);
        String password = userext.getPassword();
        //获得权限
        List<WlzxMenu> permissions = userext.getPermissions();
        List<String> permissionStrs = new ArrayList<>();
        for (WlzxMenu wlzxMenu:permissions) {
            if (StringUtils.isNotEmpty(wlzxMenu))
            permissionStrs.add(wlzxMenu.getCode());
        }

        UserJwt userJwt = new UserJwt(username,password,
                AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.join(permissions,",")));
        BeanUtils.copyProperties(userext,userJwt);
        return  userJwt;
    }
}
