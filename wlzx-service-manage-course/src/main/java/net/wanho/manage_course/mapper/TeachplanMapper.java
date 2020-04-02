package net.wanho.manage_course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wano.po.course.Teachplan;
import net.wano.po.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeachplanMapper extends BaseMapper<Teachplan> {
    TeachplanNode selectTree(String courseId);
}
