package net.wano.po.course;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


@Data
@ToString
@TableName("course_market")
public class CourseMarket implements Serializable {
    private static final long serialVersionUID = -916357110051689486L;
    @TableId
    private String id;
    private String charge;
    private String valid;
    private String qq;
    private Float price;
    private Float priceOld;
    private Date startTime;
    private Date endTime;

}
