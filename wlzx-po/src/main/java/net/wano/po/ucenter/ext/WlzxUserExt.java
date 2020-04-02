package net.wano.po.ucenter.ext;


import lombok.Data;
import lombok.ToString;
import net.wano.po.ucenter.WlzxMenu;
import net.wano.po.ucenter.WlzxUser;

import java.util.List;


@Data
@ToString
public class WlzxUserExt extends WlzxUser {

    //权限信息
    private List<WlzxMenu> permissions;

    //企业信息
    private String companyId;
}
