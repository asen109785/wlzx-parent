package net.wano.po.course.ext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class TeachplanMediaPubDocument implements Serializable {
    private String teachplan_id;
    private String media_id;
    private String media_file_original_name;
    private String media_url;
    private String course_id;
    private Date timestamp;//时间戳

}
