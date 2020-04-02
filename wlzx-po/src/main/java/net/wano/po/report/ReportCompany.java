package net.wano.po.report;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * Created by admin on 2018/2/27.
 */
@Data
@ToString
@TableName(value = "report_company")
public class ReportCompany {

    @TableId
    private String id;
    private Float evaluation_score;//评价分数（平均分）
    private Float good_scale;//好评率
    private Long course_num;//课程数
    private Long student_num;//学生人数
    private Long play_num;//播放次数
    

}
