package net.wano.po.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName(value = "cms_config_model")
public class CmsConfigModel {
    private String key;
    private String name;
    private String url;
//    private Map mapValue;
    private String value;
    private String config_id;
}
