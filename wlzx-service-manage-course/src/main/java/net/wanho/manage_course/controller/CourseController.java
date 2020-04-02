package net.wanho.manage_course.controller;

import net.wanho.api.course.CourseControllerApi;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.vo.response.PageInfo;
import net.wanho.common.web.BaseController;
import net.wanho.manage_course.service.CourseMarketService;
import net.wanho.manage_course.service.CoursePicService;
import net.wanho.manage_course.service.CourseService;
import net.wano.po.course.*;
import net.wano.po.course.ext.CourseView;
import net.wano.po.course.ext.TeachplanNode;
import net.wano.po.course.request.CourseListRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController extends BaseController implements CourseControllerApi {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CoursePicService coursePicService;

    @Autowired
    private CourseMarketService courseMarketService;



    /**
     * course 获取分页查询的 list页面
     * @param page
     * @param size
     * @param courseListRequest
     * @return
     */
    @Override
    @GetMapping("/courseBase/list/{page}/{size}")
    public AjaxResult findCourseList(@PathVariable int page, @PathVariable int size, CourseListRequest courseListRequest) {
        PageInfo pageInfo = courseService.findCourseList(page,size,courseListRequest);

        return success("查询成功",pageInfo);
    }

    /**
     * 课程计划---查询课程计划
     * @param courseId
     * @return
     */
    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable String courseId) {
        return courseService.findTeachplanList(courseId);
    }

    /**
     * 课程计划--添加课程
     * @param teachplan
     * @return
     */
    @Override
    @PostMapping("/teachplan/add")
    public AjaxResult addTeachplan(@RequestBody Teachplan teachplan) {
        courseService.addTeachplan(teachplan);
        return success();
    }


    /**
     * 添加
     * @param courseBase
     * @return
     */
    @Override
    @PostMapping("/courseBase/add")
    public AjaxResult addCourseBase(@RequestBody CourseBase courseBase) {
        courseService.addCourseBase(courseBase);
        return success();
    }

    /**
     * 修改 回显
     * @param courseId
     * @return
     * @throws RuntimeException
     */
    @Override
    @GetMapping("/courseBase/get/{courseId}")
    @PreAuthorize("hasAuthority('wlzx_teachmanager_course_base')")
    public CourseBase getCourseBaseById(@PathVariable String courseId) throws RuntimeException {
        return courseService.getById(courseId);
    }

    /**
     * . 图片查询,加载图
     * @param courseId
     * @return
     */
    @Override
    @GetMapping("/coursePic/list/{courseId}")
    @PreAuthorize("hasAuthority('course_pic_list')")
    public CoursePic findCoursePic(@PathVariable String courseId) {
        return  coursePicService.getById(courseId);
    }

    /**
     * 修改
     * @param id
     * @param courseBase
     * @return
     */
    @Override
    @PutMapping("/courseBase/update/{id}")
    public AjaxResult updateCourseBase(@PathVariable String id, @RequestBody CourseBase courseBase) {
        courseService.updateCourseBase(id,courseBase);
        return success();
    }

    /**
     * 课程营销
     * @param courseId
     * @return
     */
    @Override
    @GetMapping("/courseMarket/get/{courseId}")
    public CourseMarket getCourseMarketById(@PathVariable String courseId) {
        CourseMarket courseMarket = courseMarketService.getById(courseId);
        return courseMarket;
    }

    /**
     * 修改课程营销
     * @param id
     * @param courseMarket
     * @return
     */
    @Override
    @PostMapping("/courseMarket/update/{id}")
    public AjaxResult updateCourseMarket(@PathVariable String id, @RequestBody CourseMarket courseMarket) {
        courseMarketService.updateCourseMarket(id,courseMarket);
        return success();
    }

    /**
     * 把上传的封面 添加到数据库
     * @param courseId
     * @param pic
     * @return
     */
    @Override
    @PostMapping("/coursePic/add")
    public AjaxResult addCoursePic(String courseId,String pic) {
        //保存课程图片
        courseService.saveCoursePic(courseId,pic);
        return success();
    }



    @Override
    @DeleteMapping("/coursePic/delete")
    public AjaxResult deleteCoursePic(String courseId) {
        courseService.deleteCoursePic(courseId);
        return success();
    }


    /**
     * 课程预览--获得数据
     * @param id
     * @return
     */
    @Override
    @GetMapping("/courseView/{id}")
    public CourseView courseView(@PathVariable String id) {
        return courseService.getCourseView(id);
    }

    /**
     * 预览
     * @param id
     * @return
     */
    @Override
    @PostMapping("/preview/{id}")
    public AjaxResult preview(@PathVariable String id) {
        return success("预览成功",courseService.preview(id));
    }

    /**
     * 课程发布
     * @param id
     * @return
     */
    @Override
    @PostMapping("/publish/{id}")
    public AjaxResult publish(@PathVariable String id) {

        return success("",courseService.publish(id));
    }

    @Override
    @PostMapping("/savemedia")
    public AjaxResult saveMedia(@RequestBody TeachplanMedia teachplanMedia) {
        courseService.savemedia(teachplanMedia);
        return success();
    }
}
