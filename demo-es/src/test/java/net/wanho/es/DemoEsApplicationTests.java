package net.wanho.es;

import net.wanho.es.pojo.Student2;
import net.wanho.es.pojo.StudentRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoEsApplicationTests {
//  tempalte做查询比较好
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
//  这个做增加 和删除
    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void contextLoads() {
//        创建索引
        elasticsearchTemplate.createIndex(Student2.class);
    }

    //增加单条数据( 没有成功)
    @Test
    public void testAddSingle2(){
        Student2 stu = new Student2(101,"李五",18,"男","南京");
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(stu.getId()+"")
                .withObject(stu)
                .build();
        elasticsearchTemplate.index(indexQuery);
    }

    //增加单条数据( 成功)
    @Test
    public void testAddSingle() {
        Student2 stu = new Student2(1,"张志豪",18,"男","南京");
        studentRepository.save(stu);
        stu = new Student2(2,"张志豪2222",18,"男","上海");
        studentRepository.save(stu);
        stu = new Student2(3,"六千",18,"男","南京");
        studentRepository.save(stu);
    }

//    条件查询 +分页
    @Test
    public void testquery1() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                建立查询条件
                .withQuery(QueryBuilders.queryStringQuery("张志豪")) // ("张三  李四")多字段查询 （"id","1"）
//                分页查询
                .withPageable(PageRequest.of(0,3))
                .build();
//        查询
        AggregatedPage<Student2> student2s = elasticsearchTemplate.queryForPage(searchQuery, Student2.class);
//        通过返回对象的上下文获取
        System.out.println(student2s.getContent());
    }

    //    指定 范围 查询，+ 分页
    @Test
    public void testquery2() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                建立查询条件
                .withQuery(QueryBuilders.multiMatchQuery("张志豪","name")) // ("张三  李四")多字段查询
//                分页查询  可以指定排序，但一般不用
                .withPageable(PageRequest.of(0,3,new Sort(Sort.Direction.DESC,"id")))
                .build();
//        查询
        AggregatedPage<Student2> student2s = elasticsearchTemplate.queryForPage(searchQuery, Student2.class);
        System.out.println(student2s.getContent());
    }

    /**
     * 高亮查询
     */
    @Test
    public void testHightLight2(){
        String preTags = "<font colr=\"red\">";
        String postTags="</font>";
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                /*.withQuery(QueryBuilders.matchPhraseQuery("id","2")) //全等
                .withQuery(QueryBuilders.termQuery("name","张三2")) //like
                .withQuery(QueryBuilders.matchQuery("name","张三")) // 分词*/
                .withQuery(QueryBuilders.queryStringQuery("张志豪 南")) // 字符串
//                .withQuery(QueryBuilders.multiMatchQuery("张三 南京","name","address")) // 字符串
                .withPageable(PageRequest.of(0,2))
                .withHighlightFields(new HighlightBuilder.Field("name").preTags(preTags).postTags(postTags),
                        new HighlightBuilder.Field("address").preTags(preTags).postTags(postTags))
                .build();
        AggregatedPage<Student2> pageInfo =  elasticsearchTemplate.queryForPage(searchQuery, Student2.class,new SearchResultMapper(){
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass, Pageable pageable) {
                //定义查询出来内容存储的集合
                List<Student2> students = new ArrayList<>();
                //获取高亮的结果
                SearchHits searchHits = response.getHits();
                if(searchHits!=null) {
                    //获取高亮中所有的内容
                    SearchHit[] hits = searchHits.getHits();
                    if(hits.length > 0) {
                        for (SearchHit hit : hits) {
                            Student2 stu = new Student2();
                            //高亮结果的id值
                            String id = hit.getId();
                            //存入实体类
                            stu.setId(Integer.parseInt(id));

                            //获取第一个字段的高亮对象
                            HighlightField highlightField1 = hit.getHighlightFields().get("name");
                            //判断是否高亮，是不是就取出字符串，
                            if(highlightField1 != null) {
                                //获取第一个字段的值并封装给实体类
                                String hight_value1 = highlightField1.getFragments()[0].toString();
                                stu.setName(hight_value1);
                            }else {
                                //获取原始的值字符串
                                String value = (String) hit.getSourceAsMap().get("name");
                                stu.setName(value);
                            }
                            stu.setAge(Integer.parseInt(hit.getSourceAsMap().get("age").toString()));
                            stu.setGender(hit.getSourceAsMap().get("gender").toString());

                            //获取第二个字段的高亮内容
                            HighlightField highlightField2 = hit.getHighlightFields().get("address");
                            if(highlightField2 != null) {
                                //获取第一个字段的值并封装给实体类
                                String hight_value2 = highlightField2.getFragments()[0].toString();
                                stu.setAddress(hight_value2);
                            }else {
                                //获取原始的值
                                String value = (String) hit.getSourceAsMap().get("address");
                                stu.setAddress(value);
                            }

                            students.add(stu);

                        }
                    }
                }
                return new AggregatedPageImpl(students);
            }
        });
        System.out.println(pageInfo.getContent());
    }




}
