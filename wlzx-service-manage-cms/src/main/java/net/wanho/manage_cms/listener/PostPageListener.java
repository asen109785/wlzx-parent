package net.wanho.manage_cms.listener;

import net.wanho.common.util.StringUtils;
import net.wanho.manage_cms.service.CmsPageService;
import net.wano.po.cms.CmsPage;
import org.checkerframework.checker.units.qual.A;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostPageListener {
    @Autowired
    private CmsPageService cmsPageService;

    @RabbitListener(queues = "${wlzx.mq.queue}")
    public void receive(String pageId){
//        从消息队列中获得pageID
//        获得cmsPage对象，
        CmsPage cmsPage = cmsPageService.getById(pageId);

        if (StringUtils.isNotEmpty(cmsPage) && StringUtils.isNotEmpty(cmsPage.getHtmlFilePath())){
//            从fastdfs中下载，并保存到本地
            cmsPageService.savePageToServerPath(pageId);

        }
    }




}
