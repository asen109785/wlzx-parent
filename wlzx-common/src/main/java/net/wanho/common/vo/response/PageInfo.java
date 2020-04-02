package net.wanho.common.vo.response;

import lombok.Data;

import java.util.List;

@Data
public class PageInfo<T> {
    //数据列表
    private List<T> list;
    //数据总数
    private long total;
}
