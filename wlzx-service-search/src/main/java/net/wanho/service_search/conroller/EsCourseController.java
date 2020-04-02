package net.wanho.service_search.conroller;

import net.wanho.api.search.EsCourseControllerApi;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.web.BaseController;
import net.wanho.service_search.service.EsCourseService;
import net.wano.po.course.ext.TeachplanMediaPubDocument;
import net.wano.po.course.ext.CoursePubDocument;
import net.wano.po.search.CourseSearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/search/course")
public class EsCourseController extends BaseController implements EsCourseControllerApi {

    @Autowired
    EsCourseService esCourseService;

    /**
     * 按条件查询分页 结果
     * @param page
     * @param size
     * @param courseSearchParam
     * @return
     */
    @Override
    @GetMapping(value="/list/{page}/{size}")
    public AjaxResult list(@PathVariable int page,@PathVariable int size, CourseSearchParam courseSearchParam) {
        return success(esCourseService.list(page,size,courseSearchParam));
    }

    /**
     * 根据主键返回 map数据
     * 查询课程
     * @param id
     * @return
     */
    @Override
    @GetMapping("/getall/{id}")
    public Map<String, CoursePubDocument> getall(@PathVariable String id) {
        return esCourseService.getall(id);
    }

    /**
     * 从 es 获取对象
     * @param teachplanId
     * @return
     */
    @Override
    @GetMapping(value="/getmedia/{teachplanId}")
    public TeachplanMediaPubDocument getmedia(@PathVariable String teachplanId) {

        return esCourseService.getmedia(teachplanId);
    }
}
