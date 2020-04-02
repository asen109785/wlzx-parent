package net.wano.po.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;


@Data
@TableName(value = "sys_dictionary")
public class SysDictionary {

    @TableId
    private String id;
    private String dName;
    private String dType;

    @TableField(exist = false)
    private List<SysDictionaryValue> dValue;

}
