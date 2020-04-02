package net.wano.po.ucenter.ext;


import net.wano.po.course.ext.CategoryNode;
import lombok.Data;
import lombok.ToString;
import net.wano.po.ucenter.WlzxMenu;


import java.util.List;

/**
 * Created by admin on 2018/3/20.
 */
@Data
@ToString
public class WlzxMenuExt extends WlzxMenu {

    List<CategoryNode> children;
}
