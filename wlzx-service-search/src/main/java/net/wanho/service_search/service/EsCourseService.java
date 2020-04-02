package net.wanho.service_search.service;

import net.wanho.common.exception.ExceptionCast;
import net.wanho.common.util.StringUtils;
import net.wanho.common.vo.response.PageInfo;
import net.wanho.service_search.pojo.CoursePubDoc;
import net.wanho.service_search.pojo.TeachplanMediaPubDoc;
import net.wano.po.course.ext.CoursePubDocument;
import net.wano.po.course.ext.TeachplanMediaPubDocument;
import net.wano.po.course.response.CourseCode;
import net.wano.po.search.CourseSearchParam;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class EsCourseService {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;


    public PageInfo list(int page, int size, CourseSearchParam courseSearchParam) {
        PageInfo pageInfo = new PageInfo();
//        验证
        if (page<1){
            page = 1 ;
        }
        if (size <1){
            size = 5;
        }
        if (StringUtils.isEmpty(courseSearchParam)){
            courseSearchParam = new CourseSearchParam();
        }

//            查询
           /* 1. 根据分类搜索课程信息。
            2. 根据关键字搜索课程信息，搜索方式为全文检索，关键字需要匹配课程的名称、 课程内容。
            3. 根据难度等级搜索课程。
            4. 搜索结果分页显示。*/
//           -----------------------------------------------------------
//           查询条件 处理， 判断 + 构建
//            ----------------------------------------------------------------
            NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //        如果搜索的关键字，keyword 不为空 就进行查询
        if (StringUtils.isNotEmpty(courseSearchParam.getKeyword())) {
//            2. 根据关键字搜索课程信息，搜索方式为全文检索，关键字需要匹配课程的名称、 课程内容。
            builder.withQuery(QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(), "name", "description", "teachplan")
//               匹配度
                            .minimumShouldMatch("70%")
//               field的优先级
                            .field("name", 8)
            );
        }
//            1. 根据分类搜索课程信息。
//              1.1 一级分类
            if (StringUtils.isNotEmpty(courseSearchParam.getMt())){
                builder.withQuery(QueryBuilders.matchQuery("mt",courseSearchParam.getMt()));
            }
//              1.2 二级分类
            if (StringUtils.isNotEmpty(courseSearchParam.getSt())){
                builder.withQuery(QueryBuilders.matchQuery("st",courseSearchParam.getSt()));
            }
//            3. 根据难度等级搜索课程。
            if (StringUtils.isNotEmpty(courseSearchParam.getGrade())){
                builder.withQuery(QueryBuilders.matchQuery("grade",courseSearchParam.getGrade()));
            }
//            查询高亮： 显示高亮的设置  范围域+前后配文(前后缀)
            String preTags = "<font colr=\"red\">";
            String postTags="</font>";
            builder.withHighlightFields(new HighlightBuilder.Field("name").preTags(preTags).postTags(postTags));

            SearchQuery query = builder
//                  4. 搜索结果分页显示。
                    .withPageable(PageRequest.of(page-1,size))
                    .build();
            AggregatedPage<CoursePubDoc> aggregatedPage = elasticsearchTemplate.queryForPage(query, CoursePubDoc.class, new SearchResultMapper() {
                @Override
                public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
//                    接收查询结果+高亮结果的，list集合
                    List<CoursePubDoc> pubDocs = new ArrayList<>();
//                    获取结果的对象
                    SearchHits searchHits = searchResponse.getHits();
//                    或有所有行数
                    pageInfo.setTotal(searchHits.totalHits);
                    if (StringUtils.isNotEmpty(searchHits)) {
                        SearchHit[] hits = searchHits.getHits();
                        if (hits.length > 0) {
                            for (SearchHit hit : hits) {
                                CoursePubDoc coursePubDoc = new CoursePubDoc();
//                                源文档，每个hti的内容
                                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
//                                取出id
                                coursePubDoc.setId((String) sourceAsMap.get("id"));
//                                获取高亮范围的字段name,有则返回数组，无则null
                                HighlightField nameHigh = hit.getHighlightFields().get("name");
//                                  判断是否存在高亮
                                if (nameHigh != null) {
                                    String name = nameHigh.getFragments()[0].toString();
                                    coursePubDoc.setName(name);
                                } else {
//                                    获取原始元素
                                    coursePubDoc.setName((String) sourceAsMap.get("name"));
                                }
//                                图片
                                coursePubDoc.setPic((String) sourceAsMap.get("pic"));
//                                价格
                                coursePubDoc.setPrice((Double) sourceAsMap.get("price"));
//                                旧价格
                                coursePubDoc.setPrice_old((Double) sourceAsMap.get("price_old"));
//                                将对象放入集合中
                                pubDocs.add(coursePubDoc);
                            }
                        }
                    }

                    return new AggregatedPageImpl(pubDocs);
                }
            });
//           获取查询结果的内容
            pageInfo.setList(aggregatedPage.getContent());

        return pageInfo;
    }

    public Map<String, CoursePubDocument> getall(String id) {
//        es中根据主键查询
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(id,"id"))
                .build();
        AggregatedPage<CoursePubDoc> aggregatedPage = elasticsearchTemplate.queryForPage(searchQuery, CoursePubDoc.class);
        if (StringUtils.isEmpty(aggregatedPage)){
            ExceptionCast.cast(CourseCode.COURSE_GET_NOTEXISTS);
        }
        List<CoursePubDoc> coursePubDocs = aggregatedPage.getContent();
        Map<String, CoursePubDocument> map = new HashMap<>();
        for (CoursePubDoc coursePubDoc:coursePubDocs) {
            CoursePubDocument coursePubDocument = new CoursePubDocument();
            BeanUtils.copyProperties(coursePubDoc,coursePubDocument);
            map.put(coursePubDoc.getId(),coursePubDocument);
        }
        return map;
    }
    /**
     * 从 es 获取对象//根据课程计划查询课程媒资信息
     * @param teachplanId
     * @return
     */
    public TeachplanMediaPubDocument getmedia(String teachplanId) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(teachplanId,"teachplan_id"))
                .build();

        List<TeachplanMediaPubDoc> teachplanMediaPubDocs = elasticsearchTemplate.queryForList(searchQuery, TeachplanMediaPubDoc.class);

        //数据集合
        if(StringUtils.isNotEmpty(teachplanMediaPubDocs)){
            TeachplanMediaPubDoc teachplanMediaPubDoc = teachplanMediaPubDocs.get(0);
            TeachplanMediaPubDocument teachplanMediaPubDocument = new TeachplanMediaPubDocument();
            BeanUtils.copyProperties(teachplanMediaPubDoc,teachplanMediaPubDocument);
            return teachplanMediaPubDocument;
        }

        return null;

    }
}
