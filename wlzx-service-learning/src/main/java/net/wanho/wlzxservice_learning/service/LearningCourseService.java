package net.wanho.wlzxservice_learning.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanho.common.exception.ExceptionCast;
import net.wanho.common.util.StringUtils;

import net.wanho.wlzxservice_learning.client.CourseSearchClient;
import net.wanho.wlzxservice_learning.mapper.LearningCourseMapper;
import net.wanho.wlzxservice_learning.mapper.TaskHisMapper;
import net.wano.po.course.ext.TeachplanMediaPubDocument;
import net.wano.po.learning.LearningCourse;
import net.wano.po.learning.respones.LearningCode;
import net.wano.po.task.WlzxTask;
import net.wano.po.task.WlzxTaskHis;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class LearningCourseService extends ServiceImpl<LearningCourseMapper, LearningCourse> {

    @Autowired
    private CourseSearchClient courseSearchClient;

    //获取课程学习地址（视频播放地址）
    public String getmedia(String courseId, String teachplanId) {
        TeachplanMediaPubDocument teachplanMediaPubDocument = courseSearchClient.getmedia(teachplanId);
        if(StringUtils.isEmpty(teachplanMediaPubDocument)){
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_ERROR);
        }
        String media_url = teachplanMediaPubDocument.getMedia_url();
        return media_url;
    }

    @Resource
    private TaskHisMapper taskHisMapper;

    public LearningCourse selectByUserIdAndCourseId(String userId,String courseId){
        QueryWrapper<LearningCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        wrapper.eq("course_id",courseId);
        return  this.getOne(wrapper);
    }

    @Transactional
    public void addcourse(String userId, String courseId, String valid, Date
            startTime, Date endTime, WlzxTask wlzxTask){

        LearningCourse learningCourse = selectByUserIdAndCourseId(userId, courseId);
        if(StringUtils.isNotEmpty(learningCourse)){
            //更新选课记录
            learningCourse.setStartTime(startTime);
            learningCourse.setEndTime(endTime);
            learningCourse.setStatus("501001");
            this.updateById(learningCourse);
        }else{
            //增加选课记录
            learningCourse = new LearningCourse();
            learningCourse.setUserId(userId);
            learningCourse.setCourseId(courseId);
            learningCourse.setValid(valid);
            learningCourse.setStartTime(startTime);
            learningCourse.setEndTime(endTime);
            learningCourse.setStatus("501001");
            this.save(learningCourse);
        }

        //向历史任务表播入记录
        WlzxTaskHis wlzxTaskHis = taskHisMapper.selectById(wlzxTask.getId());
        if(StringUtils.isEmpty(wlzxTaskHis)){
            //添加历史任务
            WlzxTaskHis taskHis = new WlzxTaskHis();
            BeanUtils.copyProperties(wlzxTask,taskHis);
            taskHisMapper.insert(taskHis);
        }
    }
}
