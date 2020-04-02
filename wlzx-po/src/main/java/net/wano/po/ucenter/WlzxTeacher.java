package net.wano.po.ucenter;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("wlzx_teacher")
public class WlzxTeacher implements Serializable {
    @TableId
    private String id;
    private String name;
    private String pic;
    private String intro;
    private String resume;
    private String userId;

}
