package net.wanho.ceucenter_auth.converter;

import net.wanho.ceucenter_auth.po.UserJwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomerAuthenticationConverter extends DefaultUserAuthenticationConverter {

    /**
     * 转换器，返回信息
     * @param authentication
     * @return
     */

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        LinkedHashMap<String,Object> response = new LinkedHashMap();
        String name = authentication.getName();
        response.put("user_name",name);
        // 角色，去重
        response.put("authorities", AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
		//接收UserDetails对象
        UserJwt userJwt = (UserJwt) authentication.getPrincipal();
        response.put("id",userJwt.getId());
        response.put("name",userJwt.getName());
        response.put("utype",userJwt.getUtype());
        response.put("userpic",userJwt.getUserPic());
        response.put("companyId",userJwt.getCompanyId());
        return response;
    }
}