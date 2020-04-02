package net.wanho.manage_cms.controller;

import net.wanho.api.cms.CmsTemplateControllerApi;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.web.BaseController;
import net.wanho.manage_cms.service.CmsTemplateService;
import net.wano.po.cms.CmsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cms")
public class CmsTemplateController extends BaseController implements CmsTemplateControllerApi {

    @Autowired
    private CmsTemplateService cmsTemplateService;


    @Override
    @GetMapping("/template/{siteId}")
    public AjaxResult findAllCmsTemplate(@PathVariable String siteId) {
        List<CmsTemplate> cmsTemplates = cmsTemplateService.fandTemplasteById(siteId);
        return success(cmsTemplates);
    }
}
