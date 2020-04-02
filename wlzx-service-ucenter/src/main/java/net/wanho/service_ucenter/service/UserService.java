package net.wanho.service_ucenter.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.wanho.common.exception.ExceptionCast;
import net.wanho.common.util.StringUtils;
import net.wanho.service_ucenter.mapper.WlzxCompnayUserMapper;
import net.wanho.service_ucenter.mapper.WlzxMenuMapper;
import net.wanho.service_ucenter.mapper.WlzxUserMapper;
import net.wano.po.ucenter.WlzxCompanyUser;
import net.wano.po.ucenter.WlzxMenu;
import net.wano.po.ucenter.WlzxUser;
import net.wano.po.ucenter.ext.WlzxUserExt;
import net.wano.po.ucenter.response.AuthCode;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private WlzxUserMapper wlzxUserMapper;

    @Resource
    private WlzxMenuMapper wlzxMenuMapper;

    @Resource
    private WlzxCompnayUserMapper wlzxCompnayUserMapper;

    public WlzxUserExt getUserext(String username) {
        //根据账号查询wlzxUser信息
        QueryWrapper<WlzxUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        WlzxUser wlzxUser = wlzxUserMapper.selectOne(wrapper);
        if(StringUtils.isEmpty(wlzxUser)){
            ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
            return null;
        }

        //用户id
        String userId = wlzxUser.getId();
        //根据id查询出所有权限
        List<WlzxMenu> wlzxMenus = wlzxMenuMapper.selectPermissionByUserId(userId);

        //根据id查询出所属公司ID
        QueryWrapper<WlzxCompanyUser> w = new QueryWrapper<>();
        w.eq("user_id",userId);
        WlzxCompanyUser wlzxCompanyUser = wlzxCompnayUserMapper.selectOne(w);

        WlzxUserExt wlzxUserExt = new WlzxUserExt();
        BeanUtils.copyProperties(wlzxUser,wlzxUserExt);
        wlzxUserExt.setCompanyId(wlzxCompanyUser.getCompanyId());
        wlzxUserExt.setPermissions(wlzxMenus);
        return wlzxUserExt;

    }
}
