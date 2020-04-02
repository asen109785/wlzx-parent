package net.wano.po.course;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import java.io.Serializable;


@Data
@ToString
@Entity
@TableName("teachplan")
public class Teachplan implements Serializable {
    private static final long serialVersionUID = -916357110051689485L;
    @TableId
    private String id;
    private String pname;
    private String parentid;
    private String grade;
    private String ptype;
    private String description;
    private String courseid;
    private String status;
    private Integer orderby;
    private Double timelength;
    private String trylearn;

}
