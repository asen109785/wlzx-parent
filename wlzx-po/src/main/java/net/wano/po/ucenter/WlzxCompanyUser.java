package net.wano.po.ucenter;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("wlzx_company_user")
public class WlzxCompanyUser implements Serializable {
    @TableId
    private String id;
    private String companyId;
    private String userId;


}
