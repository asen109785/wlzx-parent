package net.wanho.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MsgListener {

    @RabbitListener( queues = "amqpAdmin.queue2")
    public void receive(String  msg){
        System.out.println(msg);
    }

}
