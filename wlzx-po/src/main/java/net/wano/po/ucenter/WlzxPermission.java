package net.wano.po.ucenter;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("wlzx_permission")
public class WlzxPermission implements Serializable {

    @TableId
    private String id;
    private String role_id;
    private String menu_id;
    private Date create_time;


}
