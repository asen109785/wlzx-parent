package net.wano.po.cms;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;



@Data
@ToString
@TableName("cms_template")
public class CmsTemplate {

    //模版ID
    @TableId
    private String templateId;
    //站点ID
    private String siteId;
    //模版名称
    private String templateName;
    //模版参数
    private String templateParameter;
    //模版内容
    private String templateContent;
}
