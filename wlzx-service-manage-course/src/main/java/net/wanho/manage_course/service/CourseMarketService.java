package net.wanho.manage_course.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanho.common.util.StringUtils;
import net.wanho.manage_course.mapper.CourseMarketMapper;
import net.wano.po.course.CourseMarket;
import org.springframework.stereotype.Service;

@Service
public class CourseMarketService extends ServiceImpl<CourseMarketMapper, CourseMarket> {



    public void updateCourseMarket(String id, CourseMarket courseMarket) {
        CourseMarket one = this.getById(id);
        //在课程营销表中没有记录，做增加操作
        if(StringUtils.isEmpty(one)){
            courseMarket.setId(id);
            this.save(courseMarket);
        }
        else {
            courseMarket.setId(id);
            //否则就是修改操作
            this.updateById(courseMarket);
        }

    }
}
