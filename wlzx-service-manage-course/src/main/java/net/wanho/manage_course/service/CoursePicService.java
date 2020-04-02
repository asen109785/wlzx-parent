package net.wanho.manage_course.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanho.manage_course.mapper.CourseMapper;
import net.wanho.manage_course.mapper.CoursePicMapper;
import net.wano.po.course.CoursePic;
import org.springframework.stereotype.Service;

@Service
public class CoursePicService extends ServiceImpl<CoursePicMapper, CoursePic> {
}
