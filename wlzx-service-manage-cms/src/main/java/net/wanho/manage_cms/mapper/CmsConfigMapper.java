package net.wanho.manage_cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wano.po.cms.CmsConfig;
import net.wano.po.cms.ext.CmsConfigExt;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CmsConfigMapper extends BaseMapper<CmsConfig> {
    CmsConfigExt getCmsConfigAndModel(String id);
}
