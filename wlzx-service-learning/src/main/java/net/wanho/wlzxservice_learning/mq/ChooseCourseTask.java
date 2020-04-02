package net.wanho.wlzxservice_learning.mq;

import com.alibaba.fastjson.JSON;

import net.wanho.wlzxservice_learning.config.RabbitmqConfig;
import net.wanho.wlzxservice_learning.service.LearningCourseService;
import net.wano.po.task.WlzxTask;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
public class ChooseCourseTask {

    @Autowired
    private LearningCourseService learningCourseService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 接收选课任务
     */
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_LEARNING_ADDCHOOSECOURSE})
    public void receiveChoosecourseTask(WlzxTask wlzxTask) throws ParseException {
        String requestBody = wlzxTask.getRequestBody();
        Map map = JSON.parseObject(requestBody, Map.class);
        String userId = (String) map.get("userId");
        String courseId = (String) map.get("courseId");
        String valid = (String) map.get("valid");
        Date startTime = null;
        Date endTime = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY‐MM‐dd HH:mm:ss");
        if(map.get("startTime")!=null){
            startTime =dateFormat.parse((String) map.get("startTime"));
        }
        if(map.get("endTime")!=null){
            endTime =dateFormat.parse((String) map.get("endTime"));
        }
        learningCourseService.addcourse(userId,courseId,valid,startTime,endTime,wlzxTask);
        //发送响应消息
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_LEARNING_ADDCHOOSECOURSE,
                RabbitmqConfig.ROUTEKEY_LEARNING_FINISHADDCHOOSECOURSE, wlzxTask.getId() );
    }
}
