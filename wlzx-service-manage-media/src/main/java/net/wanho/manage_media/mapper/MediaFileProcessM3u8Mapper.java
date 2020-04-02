package net.wanho.manage_media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wano.po.media.MediaFileProcessM3u8;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface MediaFileProcessM3u8Mapper extends BaseMapper<MediaFileProcessM3u8> {
    void batchInsert(List<MediaFileProcessM3u8> mediaFileProcessM3u8s);
}
