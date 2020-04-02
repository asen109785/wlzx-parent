package net.wanho.manage_cms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanho.manage_cms.mapper.CmsTemplateMapper;
import net.wano.po.cms.CmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmsTemplateService  extends ServiceImpl<CmsTemplateMapper, CmsTemplate> {
    public List<CmsTemplate> fandTemplasteById(String siteId) {
        QueryWrapper<CmsTemplate> wrapper = new QueryWrapper<>();
        wrapper.eq("site_id",siteId);
        List<CmsTemplate> list = this.list(wrapper);
        return list;
    }
}
