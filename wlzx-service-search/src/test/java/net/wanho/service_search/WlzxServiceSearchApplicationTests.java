package net.wanho.service_search;


import net.wanho.common.vo.response.PageInfo;
import net.wanho.service_search.pojo.TeachplanMediaPubDoc;
import net.wanho.service_search.service.EsCourseService;
import net.wano.po.course.ext.CoursePubDocument;
import net.wano.po.search.CourseSearchParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WlzxServiceSearchApplicationTests {

    @Autowired
    private EsCourseService esCourseService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void testFindByNameLike(){
        CourseSearchParam courseSearchParam = new CourseSearchParam();
        courseSearchParam.setKeyword("mysql mybatis");
        courseSearchParam.setMt("1-3");
        PageInfo<CoursePubDocument> list = esCourseService.list(1, 5, courseSearchParam);
        System.out.println(list);
    }

    //创建索引
    @Test
    public void testCreateIndex(){
        elasticsearchTemplate.createIndex(TeachplanMediaPubDoc.class);
    }

}
