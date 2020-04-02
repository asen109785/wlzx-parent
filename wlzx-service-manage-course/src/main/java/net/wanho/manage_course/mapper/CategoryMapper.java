package net.wanho.manage_course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wano.po.course.Category;
import net.wano.po.course.ext.CategoryNode;

public interface CategoryMapper extends BaseMapper<Category> {
    CategoryNode findList();
}
