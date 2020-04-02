package net.wanho.service_ucenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableEurekaClient
@EnableFeignClients
@EntityScan("net.wano.po.ucenter")

public class WlzxServiceUcenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(WlzxServiceUcenterApplication.class, args);
    }

}
