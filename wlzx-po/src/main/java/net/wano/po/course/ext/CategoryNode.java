package net.wano.po.course.ext;


import lombok.Data;
import net.wano.po.course.Category;

import java.util.List;


@Data
public class CategoryNode extends Category {
    List<CategoryNode> children;
}
