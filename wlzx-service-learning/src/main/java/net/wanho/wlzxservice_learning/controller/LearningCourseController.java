package net.wanho.wlzxservice_learning.controller;

import net.wanho.api.learning.CourseLearningControllerApi;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.web.BaseController;
import net.wanho.wlzxservice_learning.service.LearningCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/learning/course")
public class LearningCourseController extends BaseController implements CourseLearningControllerApi {

    @Autowired
    private LearningCourseService learningCourseService;

    @Override
    @GetMapping("/getmedia/{courseId}/{teachplanId}")
    public AjaxResult getmedia(@PathVariable String courseId,@PathVariable String teachplanId) {
        return success("查询成功",learningCourseService.getmedia(courseId,teachplanId));
    }
}
