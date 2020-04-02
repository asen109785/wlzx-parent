package net.wanho.manage_cms.controller;

import net.wanho.api.cms.CmsConfigControllerApi;
import net.wanho.common.web.BaseController;
import net.wanho.manage_cms.service.CmsConfigService;
import net.wano.po.cms.CmsConfig;
import net.wano.po.cms.ext.CmsConfigExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/config")
public class CmsConfigController extends BaseController implements CmsConfigControllerApi {

    @Autowired
    private CmsConfigService cmsConfigService;


    @Override
    @GetMapping("/getmodel/{id}")
    public CmsConfigExt getmodel(@PathVariable String id) {

        return cmsConfigService.getCmsConfigAndModel(id);
    }
}
