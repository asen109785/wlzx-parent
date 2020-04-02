package net.wanho.api.course;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.wanho.common.vo.response.AjaxResult;
import net.wano.po.course.*;
import net.wano.po.course.ext.CourseView;
import net.wano.po.course.ext.TeachplanNode;
import net.wano.po.course.request.CourseListRequest;
import org.springframework.web.bind.annotation.PathVariable;

@Api(value = "Teachplan管理接口", tags = "Teachplan管理接口")
public interface CourseControllerApi {

    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    public AjaxResult addTeachplan(Teachplan teachplan);

    //查询课程列表
    @ApiOperation("查询我的课程列表")
    public AjaxResult findCourseList(
            int page,
            int size,
            CourseListRequest courseListRequest
    );

    @ApiOperation("添加课程基础信息")
    public AjaxResult addCourseBase(CourseBase courseBase);

    @ApiOperation("获取课程基础信息")
    public CourseBase getCourseBaseById(String courseId) throws RuntimeException;

    @ApiOperation("更新课程基础信息")
    public AjaxResult updateCourseBase(String id, CourseBase courseBase);

    @ApiOperation("获取课程营销信息")
    public CourseMarket getCourseMarketById(String courseId);

    @ApiOperation("更新课程营销信息")
    public AjaxResult updateCourseMarket(String id, CourseMarket courseMarket);

    @ApiOperation("添加课程图片")
    public AjaxResult addCoursePic(String courseId, String pic);

    @ApiOperation("查询课程图片")
    public CoursePic findCoursePic(String courseId);

    @ApiOperation("删除课程图片")
    public AjaxResult deleteCoursePic(String courseId);

    @ApiOperation("课程视图查询")
    public CourseView courseView(String id);

    @ApiOperation("预览课程")
    public AjaxResult preview(String id);

    @ApiOperation("发布课程")
    public AjaxResult publish(@PathVariable String id);

    @ApiOperation("保存媒资信息")
    public AjaxResult saveMedia(TeachplanMedia teachplanMedia);
}
