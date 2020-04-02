package net.wanho.manage_course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wano.po.course.TeachplanMedia;
import net.wano.po.course.TeachplanMediaPub;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeachplanMediaPubMapper extends BaseMapper<TeachplanMediaPub> {
    void insertListteachplanMediaPub(List<TeachplanMediaPub> list);
}
