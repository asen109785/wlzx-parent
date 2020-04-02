package net.wanho.api.search;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.wanho.common.vo.response.AjaxResult;
import net.wano.po.course.ext.TeachplanMediaPubDocument;
import net.wano.po.course.ext.CoursePubDocument;
import net.wano.po.search.CourseSearchParam;

import java.util.Map;

/**
 * Created by Administrator.
 */
@Api(value = "课程搜索",description = "课程搜索",tags = {"课程搜索"})
public interface EsCourseControllerApi {
    //搜索课程信息
    @ApiOperation("课程综合搜索")
    public AjaxResult list(int page, int size, CourseSearchParam courseSearchParam);

    @ApiOperation("根据课程id查询课程信息")
    public Map<String, CoursePubDocument> getall(String id);

    @ApiOperation("根据课程计划id查询课程媒资信息")
    public TeachplanMediaPubDocument getmedia(String id);


}
