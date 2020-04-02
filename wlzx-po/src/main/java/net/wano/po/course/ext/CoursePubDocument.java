package net.wano.po.course.ext;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("course_pub")
public class CoursePubDocument {

    private String id;
    private String name;
    private String users;
    private String mt;
    private String st;
    private String grade;
    private String studymodel;
    private String teachmode;
    private String description;
    private String pic;//图片
    private Date timestamp;//时间戳
    private String charge;
    private String valid;
    private String qq;
    private Double price;
    private Double price_old;
    private Date expires;
    private String teachplan;//课程计划
    private Date pubTime;//课程发布时间

    private String status;
    private Date start_time;
    private Date end_time;
}