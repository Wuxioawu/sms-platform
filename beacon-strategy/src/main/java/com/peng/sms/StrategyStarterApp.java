package com.peng.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StrategyStarterApp {
    public static void main(String[] args) {
        SpringApplication.run(StrategyStarterApp.class, args);
    }
}