package net.wano.po.ucenter;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("wlzx_role")
public class WlzxRole {

    @TableId
    private String id;
    private String roleName;
    private String roleCode;
    private String description;
    private String status;
    private Date createTime;
    private Date updateTime;


}
