package net.wanho.service_ucenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wano.po.ucenter.WlzxMenu;

import java.util.List;

public interface WlzxMenuMapper extends BaseMapper<WlzxMenu> {
    List<WlzxMenu> selectPermissionByUserId(String userId);

}
