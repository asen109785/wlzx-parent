package net.wanho.manage_course.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanho.manage_course.mapper.CategoryMapper;
import net.wano.po.course.Category;
import net.wano.po.course.ext.CategoryNode;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {
    public CategoryNode findList() {
        CategoryNode categoryNode = this.baseMapper.findList();

        return categoryNode;
    }
}
