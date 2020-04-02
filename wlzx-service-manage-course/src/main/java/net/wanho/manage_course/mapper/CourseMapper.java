package net.wanho.manage_course.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import net.wano.po.course.CourseBase;
import net.wano.po.course.ext.CourseInfo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CourseMapper extends BaseMapper<CourseBase> {
    IPage<CourseInfo> selectCourseBaseAndPic(IPage<CourseInfo> ipage, @Param(Constants.WRAPPER) QueryWrapper<CourseBase> wrapper);
}
