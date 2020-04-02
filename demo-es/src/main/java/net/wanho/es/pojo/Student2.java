package net.wanho.es.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
/*
   增加时会使用面注解或属性
     索引  stu_index
     类型  student
     文档 Student2
   @Id 插入时指定es中_id的值与id属性值一致
   @Field此注解查询才会使用

     index.number_of_shards  ：一个索引应该有的主分片（primary shards）数。默认是5。而且，只能在索引创建的时候设置。（注意，每个索引的主分片数不能超过1024
     index.number_of_replicas  ：每个主分片所拥有的副本数，默认是1。
     index.refresh_interval  ：多久执行一次刷新操作，使得最近的索引更改对搜索可见。默认是1秒。设置为-1表示禁止刷新。
     indexStoreType
            fs  ：默认实现，取决于操作系统
            simplefs  ：对应Lucene SimpleFsDirectory
            niofs  ：对应Lucene NIOFSDirectory
            mmapfs  ：对应Lucene MMapDirectory
 */
@Document(indexName="stu_index",type="Student2",shards=1,replicas=0,indexStoreType="fs",refreshInterval="-1")
public class Student2 implements Serializable {
    //生成记录的id的值，不指定id值是随机的
    @Id
    private Integer id;
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer ="ik_max_word" )
    private String name;
    private Integer age;
    private String gender;
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer ="ik_max_word" )
    private String address;
}
