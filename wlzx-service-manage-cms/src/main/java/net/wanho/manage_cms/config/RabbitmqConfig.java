package net.wanho.manage_cms.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    @Value("${wlzx.mq.queue}")
    private String queue_name;
    @Value("${wlzx.mq.exchange}")
    private String exchange_name;
    @Value("${wlzx.mq.rountKey}")
    private String rount_key;

    // 创建队列
    @Bean
    public Queue queue(){

        return new Queue(queue_name,true);
    }

    // 创建direct类型交换器
    @Bean
    public Exchange exchange(){
        return  ExchangeBuilder.directExchange(exchange_name).durable(true).build();
    }

    //绑定队列到交换器
    @Bean
    public Binding binding(@Autowired Queue queue, @Autowired Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(rount_key).noargs();
    }
}
