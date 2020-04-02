package net.wanho.manage_cms.service;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import net.wanho.common.exception.ExceptionCast;
import net.wanho.common.util.StringUtils;
import net.wanho.common.vo.response.PageInfo;
import net.wanho.manage_cms.mapper.CmsPageMapper;
import net.wanho.manage_cms.mapper.CmsSiteMapper;
import net.wano.po.cms.CmsPage;
import net.wano.po.cms.CmsSite;
import net.wano.po.cms.CmsSiteServer;
import net.wano.po.cms.CmsTemplate;
import net.wano.po.cms.request.QueryPageRequest;
import net.wano.po.cms.response.CmsCode;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.*;
import java.util.Map;


@Service
@Slf4j
public class CmsPageService extends ServiceImpl<CmsPageMapper, CmsPage> {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CmsTemplateService cmsTemplateService;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CmsSiteService cmsSiteService;

    @Resource
    private CmsSiteMapper cmsSiteMapper;

    @Value("${wlzx.mq.exchange}")
    private String exchange_name;
    @Value("${wlzx.mq.rountKey}")
    private String rount_key;



    public PageInfo findList(int pageNo, int pagesize, QueryPageRequest queryPageRequest) {
        if (pageNo <1){
            pageNo = 1;
        }
        if (pagesize < 1){
            pagesize = 10;
        }

        if (StringUtils.isEmpty(queryPageRequest)){
            queryPageRequest = new QueryPageRequest();
        }

        IPage<CmsPage> page = new Page<>(pageNo,pagesize);
        QueryWrapper<CmsPage> wrapper = new QueryWrapper<>();
//        根据站点id   、模板id 、  页面别名  查询页面信息
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            wrapper.eq("site_id",queryPageRequest.getSiteId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            wrapper.eq("template_id",queryPageRequest.getTemplateId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            wrapper.eq("page_aliase",queryPageRequest.getPageAliase());
        }
        wrapper.orderByDesc("page_create_time");

        page = this.page(page, wrapper);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(page.getRecords());
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }


    public CmsPage add(CmsPage cmsPage) {
        if (StringUtils.isEmpty(cmsPage)){
            cmsPage = new CmsPage();
        }
//        先根据页面名称、站点Id、页面webpath 是否存在
        QueryWrapper<CmsPage> wrapper = new QueryWrapper<>();
        wrapper.eq("site_id",cmsPage.getSiteId());
        wrapper.eq("page_aliase",cmsPage.getPageAliase());
        wrapper.eq("page_web_path",cmsPage.getPageWebPath());
        CmsPage one = this.getOne(wrapper);
        if (StringUtils.isEmpty(one)){
            cmsPage.setPageId(null);
            this.save(cmsPage);
            return cmsPage;
        }else {
            return one;
        }


    }

    /**
     * 获取 静态化html页面内容
     * @param pageId
     * @return
     */
    public String getPageHtml(String pageId) {
//       获取模型数据-- 轮播图。。。等
        Map model = getModelByPageId(pageId);

//       根据 获得模板内容， `template_content` text COMMENT '模版文件Id' 存在fastdfs 中的模板 并下载，转化成html文本,
        String templateContent = getTemplateByPageId(pageId);
//       生成页面,把 model 数据 放入 静态化页面内，生成完整的 静态化页面  的html 的文本
        String html = generateHtml(model,templateContent);

        return html;
    }

    /**
     * 生成页面,把 model 数据 放入 静态化页面内，生成完整的 静态化页面  的html 的文本
     * @return
     */
    private String generateHtml(Map model, String templateContent) {
        //创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //创建模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", templateContent);
        configuration.setTemplateLoader(stringTemplateLoader);
        //向configuration配置模板加载器
        try {
            Template template = configuration.getTemplate("template");

            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return html;
        } catch (Exception e) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_SAVEHTMLERROR);
            return null;
        }
    }

    /**
     *       获取模型数据-- 轮播图。。。等
     * @param pageId
     * @return
     */
    private String getTemplateByPageId(String pageId) {
        CmsPage one = this.getById(pageId);
        if (StringUtils.isEmpty(one)){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String templateId = one.getTemplateId();
        if (StringUtils.isEmpty(templateId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        CmsTemplate cmsTemplate = cmsTemplateService.getById(templateId);
        String templateContent = cmsTemplate.getTemplateContent();
        String group = templateContent.substring(0,templateContent.indexOf("/"));
        String url = templateContent.substring(templateContent.indexOf("/")+1);

        byte[] bytes = fastFileStorageClient.downloadFile(group,url, new DownloadByteArray());
        String template = new String(bytes);

        return template;

    }

    /**
     *       获取模型数据-- 轮播图。。。等
     * @param pageId
     * @return
     */
    private Map getModelByPageId(String pageId) {
        CmsPage one = this.getById(pageId);
        if (StringUtils.isEmpty(one)){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String dateUrl = one.getDataUrl();
        if (StringUtils.isEmpty(dateUrl)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        Map model = restTemplate.getForObject(dateUrl, Map.class);
        return model;
    }

    /**
     * 发布
     * @param pageId
     */
    public void post(String pageId)  {

        try {
//        把预览的内容，就是模板 + 内容 的html文本上传到fastdfs
            saveHtml(pageId);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            ExceptionCast.cast(CmsCode.CMS_PAGE_FASTDFS);
        }
//        把pageId发送给rabbitmq
        sendPostPage(pageId);
    }

    /**
     *  把pageId发送给rabbitmq
     * @param pageId
     */
    private void sendPostPage(String pageId) {
//        发送不需要queue队列， 接收使用队列
        rabbitTemplate.convertAndSend(exchange_name,rount_key,pageId);


    }

    /**
     * 发布的上传 html
     * @param pageId
     */
    private void saveHtml(String pageId) throws IOException {
        String pageHtml = getPageHtml(pageId);
        if (StringUtils.isEmpty(pageHtml) || StringUtils.isEmpty(pageId)){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
//      字节数组流=内存流  上传
        InputStream is = new ByteArrayInputStream(pageHtml.getBytes());
        StorePath storePath = fastFileStorageClient.uploadFile(is, is.available(), "html", null);
//      讲htmlpage 上传的路径  更新至pageid 对应的 数据中
        CmsPage cmsPage = this.getById(pageId);
        cmsPage.setHtmlFilePath(storePath.getFullPath());
        this.updateById(cmsPage);


    }
    /**
     * 从fastdfs中下载，并保存到本地
     * @param pageId
     * @return
     */
    public void savePageToServerPath(String pageId) {
//        获取cmspage的信息
        CmsPage cmsPage = this.getById(pageId);
//        获得htmlpath
        String htmlFilePath = cmsPage.getHtmlFilePath();
//        从fastdfs下载
        String group = htmlFilePath.substring(0, htmlFilePath.indexOf("/"));
        String url = htmlFilePath.substring(htmlFilePath.indexOf("/") + 1);
        byte[] bytes = fastFileStorageClient.downloadFile(group, url, new DownloadByteArray());
//        保存在本地
//        创建输入流,内存流，读取
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
//        获得静态页面的  物理路径
        String sitePhysicalPath = cmsSiteService.getById(cmsPage.getSiteId()).getSitePhysicalPath();
        String pagePhysicalPath = cmsPage.getPagePhysicalPath();
        String pageName = cmsPage.getPageName();
        String physicalPath = sitePhysicalPath+pagePhysicalPath;
        File file = new File(physicalPath);
        if (!file.exists()){
            file.mkdirs();
        }
        String htmlPhysicalPath = sitePhysicalPath+pagePhysicalPath+pageName;

        try {
//        创建物理路径的 输出流，写入，创建一个文件 file输出流
            FileOutputStream fos = new FileOutputStream(htmlPhysicalPath);
//        使用Apache的 ioUtils ，进行拷贝
            IOUtils.copy(bis, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ExceptionCast.cast(CmsCode.CMS_POSTAGE_FIAL);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(CmsCode.CMS_POSTAGE_FIAL);
        }


    }

    /**
     * 课程发布
     * @param cmsPage
     * @return
     */
    public String postPageQuick(CmsPage cmsPage) {
        //得到页面信息
        CmsPage one = this.add(cmsPage);
        //获取页面id
        String pageId = one.getPageId();
        //调用cmsPage的发布方法
        this.post(pageId);

        //拼接页面Url= cmsSite.siteDomain+cmsSite.siteWebPath+ cmsPage.pageWebPath + cmsPage.pageName
        String siteId = one.getSiteId();
        CmsSite cmsSite = cmsSiteMapper.selectById(siteId);
        String pageUrl =cmsSite.getSiteDomain()+cmsSite.getSiteWebPath()+ one.getPageWebPath() + one.getPageName();
        return pageUrl;


    }
}
