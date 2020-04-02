package net.wanho.manage_cms;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.persistence.Entity;

@SpringBootApplication
//开启 swagger注释
@EnableSwagger2
//扫描实体类
@EntityScan("net.wanho.po.cms")
@EnableRabbit
@EnableEurekaClient
public class WlzxServiceManageCmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(WlzxServiceManageCmsApplication.class, args);
    }
}
