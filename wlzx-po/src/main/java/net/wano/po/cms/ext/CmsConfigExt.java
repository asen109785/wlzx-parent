package net.wano.po.cms.ext;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import net.wano.po.cms.CmsConfig;
import net.wano.po.cms.CmsConfigModel;

import java.util.List;


@Data
@ToString
public class CmsConfigExt extends CmsConfig {

    private List<CmsConfigModel> model;

}
