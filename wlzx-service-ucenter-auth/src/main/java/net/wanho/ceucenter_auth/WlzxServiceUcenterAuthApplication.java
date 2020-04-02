package net.wanho.ceucenter_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableEurekaClient
@EntityScan("net.wanho.po.ucenter")
@EnableFeignClients
public class WlzxServiceUcenterAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(WlzxServiceUcenterAuthApplication.class, args);
    }

}
