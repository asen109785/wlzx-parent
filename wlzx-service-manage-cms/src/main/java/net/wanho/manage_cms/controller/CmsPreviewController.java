package net.wanho.manage_cms.controller;

import lombok.extern.slf4j.Slf4j;
import net.wanho.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@Slf4j
public class CmsPreviewController {

    @Autowired
    private CmsPageService cmsPageService;

    @GetMapping(value="/cms/preview/{pageId}")
    public void preview(@PathVariable String pageId, HttpServletResponse response){
        String html = cmsPageService.getPageHtml(pageId);
        response.setHeader("content-type","text/html;charset=utf-8");
        try {
            response.getOutputStream().write(html.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

}
