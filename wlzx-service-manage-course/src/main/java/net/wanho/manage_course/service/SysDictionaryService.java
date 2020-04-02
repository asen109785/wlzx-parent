package net.wanho.manage_course.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanho.manage_course.mapper.SysDictionaryMapper;
import net.wano.po.system.SysDictionary;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysDictionaryService extends ServiceImpl<SysDictionaryMapper, SysDictionary> {

    @Resource
    private SysDictionaryMapper sysDictionaryMapper;

    public SysDictionary getByType(String type) {
        return sysDictionaryMapper.getByType(type);
    }
}
