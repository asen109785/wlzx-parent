package net.wanho.manage_media.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanho.common.util.StringUtils;
import net.wanho.common.vo.response.PageInfo;
import net.wanho.manage_media.mapper.MediaFileMapper;
import net.wano.po.media.MediaFile;
import net.wano.po.media.request.QueryMediaFileRequest;
import org.springframework.stereotype.Service;

@Service
public class MediaFileService extends ServiceImpl<MediaFileMapper, MediaFile> {
    public PageInfo<MediaFile> findList(int pageNo, int pageSize, QueryMediaFileRequest queryMediaFileRequest) {

        //验证
        if(pageNo<1){
            pageNo=1;
        }
        if(pageSize<1){
            pageSize=10;
        }
        if(StringUtils.isEmpty(queryMediaFileRequest)){
            queryMediaFileRequest = new QueryMediaFileRequest();
        }

        //分页查询
        IPage<MediaFile> page = new Page<>(pageNo,pageSize);
        QueryWrapper<MediaFile> wrapper = new QueryWrapper<>();
        //条件
        if(StringUtils.isNotEmpty(queryMediaFileRequest.getTag())){
            wrapper.eq("tag",queryMediaFileRequest.getTag());
        }
        if(StringUtils.isNotEmpty(queryMediaFileRequest.getFileOriginalName())){
            wrapper.eq("file_original_name",queryMediaFileRequest.getFileOriginalName());
        }
        if(StringUtils.isNotEmpty(queryMediaFileRequest.getProcessStatus())){
            wrapper.eq("process_status",queryMediaFileRequest.getProcessStatus());
        }
        page=this.page(page,wrapper);

        PageInfo<MediaFile> pageInfo = new PageInfo<>();

        pageInfo.setTotal(page.getTotal());
        pageInfo.setList(page.getRecords());
        return pageInfo;
    }
}
