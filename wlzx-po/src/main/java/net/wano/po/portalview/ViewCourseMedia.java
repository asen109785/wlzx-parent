package net.wano.po.portalview;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by admin on 2018/2/27.
 */
@Data
@ToString
@TableName(value = "view_course_media")
public class ViewCourseMedia implements Serializable{

    @TableId
    private String teachplanId;

    private String mediaId;

    private String mediaFileOriginalName;

    private String mediaUrl;
    private String courseId;

}
