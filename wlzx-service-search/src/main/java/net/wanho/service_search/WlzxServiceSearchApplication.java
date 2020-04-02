package net.wanho.service_search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class WlzxServiceSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(WlzxServiceSearchApplication.class, args);
    }

}
