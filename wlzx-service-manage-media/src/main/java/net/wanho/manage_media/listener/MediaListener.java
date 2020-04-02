package net.wanho.manage_media.listener;

import net.wanho.common.util.HlsVideoUtil;
import net.wanho.common.util.Mp4VideoUtil;
import net.wanho.common.util.StringUtils;
import net.wanho.manage_media.mapper.MediaFileMapper;
import net.wanho.manage_media.mapper.MediaFileProcessM3u8Mapper;
import net.wano.po.media.MediaFile;
import net.wano.po.media.MediaFileProcessM3u8;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class MediaListener {

    @Resource
    private MediaFileMapper mediaFileMapper;

    @Resource
    private MediaFileProcessM3u8Mapper mediaFileProcessM3u8Mapper;

    @Value("${wlzx.ffmpeg-path}")
    private String ffmpeg_path;
    @Value("${wlzx.upload-location}")
    private String upload_location;

    @RabbitListener(queues = "${wlzx.mq.queue}",containerFactory = "customContainerFactory")
    @Transactional
    public void receiveMediaTask(String fileId){
        MediaFile mediaFile = mediaFileMapper.selectById(fileId);
        if(StringUtils.isEmpty(mediaFile)){
            return;
        }

        //文件类型的判断，暂时就支持wmv格式转mp4
        String fileType = mediaFile.getFileType();
        if(!fileType.equalsIgnoreCase("wmv")){
            mediaFile.setProcessStatus("303004");
            mediaFileMapper.updateById(mediaFile);
            return;
        }

        //处理中
        mediaFile.setProcessStatus("303001");
        mediaFileMapper.updateById(mediaFile);

        //转mp4
        String video_path = upload_location + mediaFile.getFilePath()+mediaFile.getFileName();
        String mp4_name=fileId+".mp4";
        String mp4folder_path=upload_location+mediaFile.getFilePath();
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path,video_path,mp4_name,mp4folder_path);
        mp4VideoUtil.generateMp4();

        //转m3u8
        String m3u8_name = fileId+".m3u8";
        String mp4_video_path = mp4folder_path+mp4_name;
        String m3u8folder_path=upload_location+mediaFile.getFilePath()+"hls/";

        HlsVideoUtil hlsVideoUtil =  new HlsVideoUtil(ffmpeg_path,mp4_video_path,m3u8_name,m3u8folder_path);
        hlsVideoUtil.generateM3u8();
        mediaFile.setFileUrl(mediaFile.getFilePath()+"hls/"+m3u8_name);
        mediaFile.setProcessStatus("303002");
        mediaFileMapper.updateById(mediaFile);

        //保存m3u8记录到数据库
        List<MediaFileProcessM3u8> mediaFileProcessM3u8s = new ArrayList<>();
        for(String item:  hlsVideoUtil.get_ts_list()){
            MediaFileProcessM3u8 one = new MediaFileProcessM3u8();
            one.setTslist(item);
            one.setMediaFileId(mediaFile.getFileId());
            mediaFileProcessM3u8s.add(one);
        }
        mediaFileProcessM3u8Mapper.batchInsert(mediaFileProcessM3u8s);

    }
}
