package com.peng.sms.api;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.peng.sms.api", "com.peng.sms.util"})
public class APIStarterAPP {
    public static void main(String[] args) {
        SpringApplication.run(APIStarterAPP.class, args);
    }
}