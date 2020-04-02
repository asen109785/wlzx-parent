package net.wano.po.cms;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;


@Data
@TableName(value = "cms_config")
public class CmsConfig {

  @TableId
    private String id;
    private String name;

}
