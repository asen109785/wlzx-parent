package net.wano.po.course.ext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.wano.po.course.CourseBase;
import net.wano.po.course.CourseMarket;
import net.wano.po.course.CoursePic;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseView implements Serializable {
    CourseBase courseBase;//基础信息
    CourseMarket courseMarket;//课程营销
    CoursePic coursePic;//课程图片
    TeachplanNode TeachplanNode;//教学计划
}