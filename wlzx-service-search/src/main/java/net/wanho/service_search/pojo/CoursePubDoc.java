package net.wanho.service_search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "wlzx_course",shards = 1,replicas = 0,type = "CoursePub")
public class CoursePubDoc {

    @Id
    private String id;
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer ="ik_max_word" )
    private String name;
    private String users;
    private String mt;
    private String st;
    private String grade;
    private String studymodel;
    private String teachmode;
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer ="ik_max_word" )
    private String description;
    private String pic;//图片
    @Field(type = FieldType.Date,format = DateFormat.basic_date_time)
    private Date timestamp;//时间戳
    private String charge;
    private String valid;
    private String qq;
    private Double price;
    private Double price_old;
    @Field(type = FieldType.Date,format = DateFormat.basic_date_time)
    private Date expires;
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer ="ik_max_count" )
    private String teachplan;//课程计划
    @Field(type = FieldType.Date,format = DateFormat.basic_date_time)
    private Date pubTime;//课程发布时间

    private String status;
    @Field(type = FieldType.Date,format = DateFormat.basic_date_time)
    private Date start_time;
    @Field(type = FieldType.Date,format = DateFormat.basic_date_time)
    private Date end_time;
}
