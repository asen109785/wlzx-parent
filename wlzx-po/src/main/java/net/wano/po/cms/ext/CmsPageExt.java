package net.wano.po.cms.ext;

import net.wano.po.cms.CmsPage;
import lombok.Data;
import lombok.ToString;



@Data
@ToString
public class CmsPageExt extends CmsPage {
    private String htmlValue;

}
