package net.wano.po.course;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;


@Data
@ToString
@Entity
@TableName("course_off")
public class CourseOff implements Serializable {
    private static final long serialVersionUID = -916357110051689488L;
    @TableId
    private String id;
    private String name;
    private String users;
    private String mt;
    private String st;
    private String grade;
    private String studymodel;
    private String description;
    private String pic;//图片
    private Date timestamp;//时间戳
    private String charge;
    private String valid;
    private String qq;
    private Float price;
    private Float price_old;
    private Date expires;
    private String teachplan;//课程计划


}
