package net.wanho.manage_course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wano.po.system.SysDictionary;

public interface SysDictionaryMapper extends BaseMapper<SysDictionary> {
    SysDictionary getByType(String type);
}
