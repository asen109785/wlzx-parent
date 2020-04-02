package net.wanho.manage_course.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.wanho.common.exception.ExceptionCast;
import net.wanho.common.util.StringUtils;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.vo.response.CommonCode;
import net.wanho.common.vo.response.PageInfo;
import net.wanho.manage_course.client.CmsPageClient;
import net.wanho.manage_course.mapper.*;
import net.wano.po.cms.CmsPage;
import net.wano.po.cms.request.QueryPageRequest;
import net.wano.po.course.*;
import net.wano.po.course.ext.CourseInfo;
import net.wano.po.course.ext.CourseView;
import net.wano.po.course.ext.TeachplanNode;
import net.wano.po.course.request.CourseListRequest;
import net.wano.po.course.response.CourseCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CourseService extends ServiceImpl<CourseMapper, CourseBase> {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private CoursePicMapper coursePicMapper;

    @Resource
    private TeachplanMapper teachplanMapper;

    @Resource
    private CourseMarketMapper courseMarketMapper;

    @Resource
    private CmsPageClient cmsPageClient;

    @Resource
    private CoursePubMapper coursePubMapper;

    @Resource
    private TeachplanMediaMapper teachplanMediaMapper;

    @Resource
    private TeachplanMediaPubMapper teachplanMediaPubMapper;

    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;

    public PageInfo findCourseList(int page, int size, CourseListRequest courseListRequest) {
        if (page <1){
            page = 1;
        }
        if (size < 1){
            size = 10;
        }

        if (StringUtils.isEmpty(courseListRequest)){
            courseListRequest = new CourseListRequest();
        }
//        暂时写死
        courseListRequest.setCompanyId("2");

        IPage<CourseInfo> ipage = new Page<>(page,size);
        QueryWrapper<CourseBase> wrapper = new QueryWrapper<>();
        wrapper.eq("company_id",courseListRequest.getCompanyId());
        ipage = this.baseMapper.selectCourseBaseAndPic(ipage,wrapper);

        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(ipage.getRecords());
        pageInfo.setTotal(ipage.getTotal());

        return pageInfo;
    }

    /**
     * 添加
     * @param courseBase
     */
    public void addCourseBase(CourseBase courseBase) {
        //课程状态默认为未发布
        courseBase.setStatus("202001");
        //todo componyId以后从登录用户中获取
        courseBase.setCompanyId("2");
        courseMapper.insert(courseBase);
    }

    /**
     * 修改
     * @param id
     * @param courseBase
     */
    public void updateCourseBase(String id, CourseBase courseBase) {
        CourseBase one = this.getById(id);
        if(StringUtils.isEmpty(one)){
//            课程不存在
            ExceptionCast.cast(CourseCode.COURSE_GET_NOTEXISTS);
        }
        //修改课程信息
        courseBase.setId(id);
        this.updateById(courseBase);
    }

    public void saveCoursePic(String courseId, String pic) {
        CourseBase one = this.getById(courseId);
        if(StringUtils.isEmpty(one)){
            ExceptionCast.cast(CourseCode.COURSE_GET_NOTEXISTS);
        }

        CoursePic onePic = coursePicMapper.selectById(courseId);
        if(StringUtils.isNotEmpty(onePic)){
            ExceptionCast.cast(CourseCode.COURSE_PIC_EXISTS);
        }
        else {
            //保存到course_pic表中
            CoursePic coursePic = new CoursePic();
            coursePic.setCourseid(courseId);
            coursePic.setPic(pic);
            coursePicMapper.insert(coursePic);
        }
    }

    public void deleteCoursePic(String courseId) {
        if (StringUtils.isEmpty(courseId)){
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_NOTEXISTS);
        }
        coursePicMapper.deleteById(courseId);
    }

    /**
     * 课程计划---查询课程计划
     * @param courseId
     * @return
     */
    public TeachplanNode findTeachplanList(String courseId) {
        return teachplanMapper.selectTree(courseId);
    }

    /**
     * 课程计划---添加课程
     * @param teachplan
     */
    public void addTeachplan(Teachplan teachplan) {

        if(teachplan==null || StringUtils.isEmpty(teachplan.getCourseid()) ||StringUtils.isEmpty(teachplan.getPname()) ){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //课程id
        String courseid = teachplan.getCourseid();
        //父节点的id
        String parentid = teachplan.getParentid();

        //当没有选父节点的时候，则应该添加是二级节点，它应该添加一级节点下
        if(StringUtils.isEmpty(parentid)){
            //获取课程的根结点
            parentid=getTeachplanRoot(courseid);
        }

        //取出父结点信息
        Teachplan parentTeachplan = teachplanMapper.selectById(parentid);

        //父节点的级别
        String parentGrade = parentTeachplan.getGrade();


        //设置其它值
        teachplan.setParentid(parentid);
        if(parentGrade.equals("1"))
            teachplan.setGrade("2");
        else
            teachplan.setGrade("3");
        teachplanMapper.insert(teachplan);


    }

    //获取课程的根结点
    private String getTeachplanRoot(String courseId) {

        //调用mapper查询teachplan表得到该课程的根结点（一级节点）
        QueryWrapper<Teachplan> wrapper = new QueryWrapper<>();
        wrapper.eq("courseid",courseId);
        wrapper.eq("parentid","0");
        Teachplan  rootTeachplan = teachplanMapper.selectOne(wrapper);

        CourseBase courseBase = courseMapper.selectById(courseId);

        //在teachplan查不到根节点
        if(StringUtils.isEmpty(rootTeachplan)){
            //要增加一级节点，一级节点名称即课程的名称
            Teachplan teachplan = new Teachplan();
            teachplan.setCourseid(courseId);//所属课程
            teachplan.setParentid("0");//一级结点
            teachplan.setGrade("1");//一级结点
            teachplan.setPname(courseBase.getName());//一级节点名称即课程的名称
            teachplan.setStatus("0");//未发布
            teachplanMapper.insert(teachplan);
            return teachplan.getId();
        }
        return rootTeachplan.getId();


    }

    public CourseView getCourseView(String id) {
        CourseView courseView = new CourseView();
        //查询课程基本信息
        CourseBase courseBase= courseMapper.selectById(id);
        courseView.setCourseBase(courseBase);
        //查询课程营销信息
        CourseMarket courseMarket= courseMarketMapper.selectById(id);
        courseView.setCourseMarket(courseMarket);
        //查询课程图片信息
        CoursePic coursePic = coursePicMapper.selectById(id);
        courseView.setCoursePic(coursePic);
        //查询课程计划信息
        TeachplanNode teachplanNode = teachplanMapper.selectTree(id);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }

    //课程预览
    public String preview(String courseId) {
        CourseBase courseBase = this.getById(courseId);
        //发布课程预览页面
        CmsPage cmsPage = new CmsPage();
        //站点
        cmsPage.setSiteId(publish_siteId);//课程预览站点
        //模板
        cmsPage.setTemplateId(publish_templateId);
        //页面名称
        cmsPage.setPageName(courseId + ".html");
        //页面别名
        cmsPage.setPageAliase(courseBase.getName());
        //页面访问路径
        cmsPage.setPageWebPath(publish_page_webpath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        //数据url
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);
        //远程请求cms保存页面信息
        AjaxResult cmsPageResult = cmsPageClient.save(cmsPage);

        if(!cmsPageResult.isSuccess()){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_VIEWERROR);
        }
        //页面id
        String pageId = cmsPageResult.getData().toString();
        //页面url
        String pageUrl = previewUrl + pageId;
        return pageUrl;
    }

    /**
     * 课程发布
     * @param courseId
     * @return
     */
    public String publish(String courseId) {
//        获得 courseBase 信息
        CourseBase courseBase = this.getById(courseId);
//        设置课程站点信息
        //发布课程预览页面
        CmsPage cmsPage = new CmsPage();
        //站点
        cmsPage.setSiteId(publish_siteId);//课程预览站点
        //模板
        cmsPage.setTemplateId(publish_templateId);
        //页面名称
        cmsPage.setPageName(courseId + ".html");
        //页面别名
        cmsPage.setPageAliase(courseBase.getName());
        //页面访问路径
        cmsPage.setPageWebPath(publish_page_webpath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        //数据url
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);
        //远程请求cms的postPageQuick方法发布课程信息
        AjaxResult ajaxResult = cmsPageClient.postPageQuick(cmsPage);
        if (!ajaxResult.isSuccess()){
        //创建课程详情页出错
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_CDETAILERROR);
        }
//        保存课程发布状态为 已发布
        this.changeCourseBaseState(courseId);
//        获取CoursePub信息
        CoursePub coursePub = createCoursePub(courseId);
        //保存发布信息
        this.saveCoursePub(courseId,coursePub);

//        保存teachplanmediapub 信息
        this.saveTeachpanMediaPub(courseId);

        //得到页面的url
        String pageUrl = ajaxResult.getData().toString();
        return pageUrl;

    }

    /**
     *  向teachplanMediaPub中保存课程媒资信息
     *  1) 根据课程id删除teachplanMediaPub中的数据
     *  2）根据课程id查询teachplanMedia数据
     *  3）将查询到的teachplanMedia数据插入到teachplanMediaPub中
     */
    private void saveTeachpanMediaPub(String courseId) {
        QueryWrapper<TeachplanMediaPub> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        teachplanMediaPubMapper.delete(queryWrapper);

        QueryWrapper<TeachplanMedia> queryWrapper1 = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        List<TeachplanMedia> teachplanMedias = teachplanMediaMapper.selectList(queryWrapper1);

//        批量插入，不能循环 insert效率低
//        把TeachplanMedia 数据 拷贝到 pun
        List<TeachplanMediaPub> list = new ArrayList<>();
        for (TeachplanMedia teachplanMedia:teachplanMedias) {
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            BeanUtils.copyProperties(teachplanMedia,teachplanMediaPub);
            teachplanMediaPub.setTimestamp(new Date());
            list.add(teachplanMediaPub);
        }
        teachplanMediaPubMapper.insertListteachplanMediaPub(list);
    }

    //  保存pub 信息
    private void saveCoursePub(String courseId, CoursePub coursePub) {
        CoursePub one = coursePubMapper.selectById(courseId);
        if (StringUtils.isNotEmpty(one)){
//            更新pub 的信息
            BeanUtils.copyProperties(coursePub,one);
//            重置主键
            one.setId(courseId);
//            时间戳,给logstach使用
            one.setTimestamp(new Date());
//            发布时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String day = sdf.format(new Date());
            one.setPubTime(day);
//            修改pub发布信息
            coursePubMapper.updateById(one);
        }else {
//            如果没有，就是第一次发布，就新增
//            **把one创建内存空间
            one = new CoursePub();
//            更新pub 的信息
            BeanUtils.copyProperties(coursePub,one);
//            重置主键
            one.setId(courseId);
//            时间戳,给logstach使用
            one.setTimestamp(new Date());
//            发布时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String day = sdf.format(new Date());
            one.setPubTime(day);
//            新增
            coursePubMapper.insert(one);
        }
    }

    //        获取CoursePub信息
    private CoursePub createCoursePub(String courseId) {
        CoursePub coursePub = new CoursePub();

//        获取couseBase，课程基本信息
        CourseBase courseBase = courseMapper.selectById(courseId);
        BeanUtils.copyProperties(courseBase,coursePub);
//        获取课程图片，并拷贝
        CoursePic coursePic = coursePicMapper.selectById(courseId);
        BeanUtils.copyProperties(coursePic,coursePub);
//        课程营销信息
        CourseMarket courseMarket =  courseMarketMapper.selectById(courseId);
        BeanUtils.copyProperties(courseMarket, coursePub);

//        课程计划信息
        TeachplanNode teachplanNode = teachplanMapper.selectTree(courseId);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //吧 课程计划变成json字符串存入 pub 中
            coursePub.setTeachplan(objectMapper.writeValueAsString(teachplanNode));
            return coursePub;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    //        保存课程发布状态为 已发布
    private void changeCourseBaseState(String courseId) {
        CourseBase courseBase = this.getById(courseId);
        courseBase.setStatus("202002");
        courseMapper.updateById(courseBase);
    }

    public void savemedia(TeachplanMedia teachplanMedia) {
        if(teachplanMedia == null || StringUtils.isEmpty(teachplanMedia.getTeachplanId())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }

        //校验课程计划是否是3级
        //课程计划
        String teachplanId = teachplanMedia.getTeachplanId();
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if(StringUtils.isEmpty(teachplan)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }

        String grade = teachplan.getGrade();
        if(StringUtils.isEmpty(grade) || !grade.equals("3")) {
            //只允许选择第三级的课程计划关联视频
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_GRADEERROR);
        }

        //关联视频
        TeachplanMedia one = teachplanMediaMapper.selectById(teachplanMedia.getTeachplanId());
        if(StringUtils.isEmpty(one)){
            teachplanMediaMapper.insert(teachplanMedia);
        }else{
            teachplanMediaMapper.updateById(teachplanMedia);
        }
    }

}

