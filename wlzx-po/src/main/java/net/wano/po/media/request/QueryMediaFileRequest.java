package net.wano.po.media.request;


import lombok.Data;
import net.wanho.common.vo.request.RequestData;


@Data
public class QueryMediaFileRequest extends RequestData {

    private String fileOriginalName;
    private String processStatus;
    private String tag;
}
