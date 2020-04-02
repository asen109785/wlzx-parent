package net.wano.po.test;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import java.util.Date;

/**
 * Created by mrt on 2018/3/30.
 */
@Data
@ToString
@TableName(value = "user_test")
public class UserTest {


    @TableId
    private String id;
    private String name;

    @Column(name="create_time")
    private Date createTime;
}
