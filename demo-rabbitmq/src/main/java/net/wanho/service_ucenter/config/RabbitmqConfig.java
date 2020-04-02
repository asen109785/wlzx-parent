package net.wanho.service_ucenter.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Bean
    public Queue getQueue(){
        return new Queue("amqpAdmin.queue2",true);
    }

    @Bean
    public Exchange getExchange(){
        return  ExchangeBuilder.directExchange("amqpAdmin.direct2").durable(true).build();
    }

    @Bean
    public Binding getBinding(@Autowired Queue queue, @Autowired Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("amqpAdmin.wanho2").noargs();
    }
}
