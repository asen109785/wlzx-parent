package net.wanho.wlzxservice_learning;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableSwagger2
@EnableRabbit
@EntityScan("net.wano.po.learning")
public class WlzxServiceLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(WlzxServiceLearningApplication.class, args);
    }

}
