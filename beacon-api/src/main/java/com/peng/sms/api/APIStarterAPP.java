package com.peng.sms.api;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class APIStarterAPP {
    public static void main(String[] args) {
        SpringApplication.run(APIStarterAPP.class, args);
    }
}