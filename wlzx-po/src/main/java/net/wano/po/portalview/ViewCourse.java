package net.wano.po.portalview;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import net.wano.po.course.CourseBase;
import net.wano.po.course.CourseMarket;
import net.wano.po.course.CoursePic;
import net.wano.po.course.ext.TeachplanNode;
import net.wano.po.report.ReportCourse;

import java.io.Serializable;

/**
 * Created by admin on 2018/2/27.
 */
@Data
@ToString
@TableName(value = "view_course")
public class ViewCourse implements Serializable{

    @TableId
    private String id;
    private CourseBase courseBase;
    private CourseMarket courseMarket;
    private CoursePic coursePic;
    private TeachplanNode teachplan;
    //课程统计信息
    private ReportCourse reportCourse;

}
