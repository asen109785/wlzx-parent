package net.wano.po.media;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import net.wanho.common.util.MD5Util;


import java.util.List;


/**
 * Created by admin on 2018/3/5.
 */
@Data
@ToString
@TableName(value = "media_video_course")
public class MediaVideoCourse {

    @TableId
    private String id;
    //课程id
    private String courseid;
    //章节id
    private String chapter;
    //文件id
    private String fileId;
    //视频处理方式
    private String processType;
    //视频处理状态
    private String processStatus;
    //HLS处理结果
    private String hls_m3u8;
    private List<String> hls_ts_list;

    public MediaVideoCourse(String fileId,String courseid,String chapter){
        this.fileId = fileId;
        this.courseid = courseid;
        this.chapter = chapter;
        this.id = MD5Util.getStringMD5(courseid+chapter);
        this.processType = "302002";//生成 hls
        this.processStatus="303001";//未处理
    }

}
