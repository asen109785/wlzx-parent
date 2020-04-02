package net.wanho.api.course;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.wano.po.course.ext.CategoryNode;

@Api(value="课程分类管理接口",tags = "课程分类管理接口，提供课程分类的管理、查询")
public interface CategoryControllerApi {
    @ApiOperation("查询分类")
    public CategoryNode findList();
}
