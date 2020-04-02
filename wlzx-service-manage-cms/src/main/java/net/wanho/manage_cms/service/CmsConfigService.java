package net.wanho.manage_cms.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanho.manage_cms.mapper.CmsConfigMapper;
import net.wano.po.cms.CmsConfig;
import net.wano.po.cms.ext.CmsConfigExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CmsConfigService extends ServiceImpl<CmsConfigMapper, CmsConfig> {

    @Resource
    private CmsConfigMapper cmsConfigMapper;

    public CmsConfigExt getCmsConfigAndModel(String id) {
        return cmsConfigMapper.getCmsConfigAndModel(id);

    }
}
