package net.wano.po.course.request;


import lombok.Data;
import net.wanho.common.vo.request.RequestData;


@Data
public class CourseListRequest extends RequestData {
    //公司Id
    private String companyId;
}
