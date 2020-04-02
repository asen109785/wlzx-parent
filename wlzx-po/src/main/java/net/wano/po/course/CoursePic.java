package net.wano.po.course;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("course_pic")
public class CoursePic implements Serializable {
    @TableId
    private String courseid;
    private String pic;
}
