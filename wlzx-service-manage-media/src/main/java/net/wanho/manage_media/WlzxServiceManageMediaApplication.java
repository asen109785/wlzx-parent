package net.wanho.manage_media;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EntityScan("net.wanho.po.media")
@EnableSwagger2
@EnableRabbit
public class WlzxServiceManageMediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WlzxServiceManageMediaApplication.class, args);
    }

}
