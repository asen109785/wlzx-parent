package net.wanho.wlzxservice_learning.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitmqConfig {

    //添加选课任务交换机
    public static final String EX_LEARNING_ADDCHOOSECOURSE = "ex_learning_addchoosecourse";
    //添加选课消息队列
    public static final String QUEUE_LEARNING_ADDCHOOSECOURSE = "queue_learning_addchoosecourse";
    //完成添加选课消息队列
    public static final String QUEUE_LEARNING_FINISHADDCHOOSECOURSE =
            "queue_learning_finishaddchoosecourse";
    //添加选课路由key
    public static final String ROUTEKEY_LEARNING_ADDCHOOSECOURSE = "addchoosecourse";
    //完成添加选课路由key
    public static final String ROUTEKEY_LEARNING_FINISHADDCHOOSECOURSE = "finishaddchoosecourse";

    /**
     * 交换机配置使用direct类型
     * @return the exchange
     */
    @Bean(EX_LEARNING_ADDCHOOSECOURSE)
    public Exchange EXCHANGE_TOPICS_INFORM() {
        return ExchangeBuilder.directExchange(EX_LEARNING_ADDCHOOSECOURSE).durable(true).build();
    }

    /**
     * 声明队列
     * @return
     */
    @Bean(QUEUE_LEARNING_ADDCHOOSECOURSE)
    public Queue QUEUE_LEARNING_ADDCHOOSECOURSE() {
        Queue queue = new Queue(QUEUE_LEARNING_ADDCHOOSECOURSE);
        return queue;
    }
    @Bean(QUEUE_LEARNING_FINISHADDCHOOSECOURSE)
    public Queue QUEUE_LEARNING_FINISHADDCHOOSECOURSE() {
        Queue queue = new Queue(QUEUE_LEARNING_FINISHADDCHOOSECOURSE);
        return queue;
    }


    /**
     * 绑定队列到交换机
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding BINDING_QUEUE_ADDCHOOSECOURSE(@Qualifier(QUEUE_LEARNING_ADDCHOOSECOURSE) Queue queue, @Qualifier(EX_LEARNING_ADDCHOOSECOURSE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTEKEY_LEARNING_ADDCHOOSECOURSE).noargs();
    }

    @Bean
    public Binding BINDING_QUEUE_FINISHADDCHOOSECOURSE(@Qualifier(QUEUE_LEARNING_FINISHADDCHOOSECOURSE) Queue queue, @Qualifier(EX_LEARNING_ADDCHOOSECOURSE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTEKEY_LEARNING_FINISHADDCHOOSECOURSE).noargs();
    }

}

