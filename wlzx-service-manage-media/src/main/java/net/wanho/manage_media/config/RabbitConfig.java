package net.wanho.manage_media.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${wlzx.mq.exchange}")
    private String exchange_name;
    @Value("${wlzx.mq.queue}")
    private String quque_name;
    @Value("${wlzx.mq.routingKey}")
    private String routing_key;

    @Bean
    public Exchange exchange(){
        return ExchangeBuilder.directExchange(exchange_name).durable(true).build();
    }

    @Bean
    public Queue queue(){
        return new Queue(quque_name,true);
    }

    @Bean
    public Binding binding(@Autowired Queue queue, @Autowired Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(routing_key).noargs();
    }

    //消费者并发数量
    public static final int DEFAULT_CONCURRENT = 10;

    @Bean("customContainerFactory")
    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(DEFAULT_CONCURRENT);
        factory.setMaxConcurrentConsumers(DEFAULT_CONCURRENT);
        configurer.configure(factory, connectionFactory);
        return factory;
    }


}
