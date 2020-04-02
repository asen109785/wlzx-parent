package net.wanho.service_search.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Document(indexName = "wlzx_course_media",shards = 1,replicas = 0,type = "TeachplanMediaPub")
public class TeachplanMediaPubDoc implements Serializable {

    @Id
    private String teachplan_id;

    private String media_id;
    private String media_file_original_name;
    private String media_url;
    private String course_id;
    @Field(type = FieldType.Date,format = DateFormat.basic_date_time)
    private Date timestamp;//时间戳

}
