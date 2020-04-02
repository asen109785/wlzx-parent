package net.wanho.api.learning;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.wanho.common.vo.response.AjaxResult;

/**
 * Created by Administrator.
 */
@Api(value = "录播课程学习管理",tags = "录播课程学习管理")
public interface CourseLearningControllerApi {

    @ApiOperation("获取课程学习地址")
    public AjaxResult getmedia(String courseId, String teachplanId);
}
