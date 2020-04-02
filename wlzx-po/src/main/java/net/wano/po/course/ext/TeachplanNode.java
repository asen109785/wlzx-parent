package net.wano.po.course.ext;


import lombok.Data;
import net.wano.po.course.Teachplan;

import java.util.List;


@Data
public class TeachplanNode extends Teachplan {
    //媒资文件id
    String mediaId;
    //媒资文件原始名称
    String mediaFileOriginalName;

    List<TeachplanNode> children;
}
