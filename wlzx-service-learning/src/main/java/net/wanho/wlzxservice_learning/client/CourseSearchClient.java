package net.wanho.wlzxservice_learning.client;

import net.wanho.common.client.WlzxServiceList;
import net.wano.po.course.ext.TeachplanMediaPubDocument;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient("wlzx-service-search")
@FeignClient(WlzxServiceList.WLZX_SERVICE_SEARCH)
public interface CourseSearchClient {

    //根据课程计划id查询课程媒资
    @GetMapping("/search/course/getmedia/{teachplanId}")
    public TeachplanMediaPubDocument getmedia(@PathVariable String teachplanId);
}
