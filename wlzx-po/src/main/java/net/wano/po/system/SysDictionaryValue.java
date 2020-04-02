package net.wano.po.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "sys_dictionary_value")
public class SysDictionaryValue {

    @TableId
    private String sdId;
    private String sdName;
    private String sdStatus;

}
