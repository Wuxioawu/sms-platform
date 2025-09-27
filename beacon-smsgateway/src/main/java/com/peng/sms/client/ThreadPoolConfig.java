package com.peng.sms.client;

import cn.hippo4j.core.executor.DynamicThreadPool;
import cn.hippo4j.core.executor.support.ThreadPoolBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    @DynamicThreadPool
    public ThreadPoolExecutor cmppSubmitPool() {
        String threadPoolId = "cmpp-submit";
        ThreadPoolExecutor messageConsumeDynamicExecutor = ThreadPoolBuilder.builder()
                // Specify the prefix for thread names
                .threadFactory(threadPoolId)
                // The unique identifier for the thread pool in Hippo4j
                .threadPoolId(threadPoolId)
                // Represents a dynamic thread pool
                .dynamicPool()
                .build();
        return messageConsumeDynamicExecutor;
    }

    @Bean
    @DynamicThreadPool
    public ThreadPoolExecutor cmppDeliverPool() {
        String threadPoolId = "cmpp-deliver";
        ThreadPoolExecutor messageProduceDynamicExecutor = ThreadPoolBuilder.builder()
                .threadFactory(threadPoolId)
                .threadPoolId(threadPoolId)
                .dynamicPool()
                .build();
        return messageProduceDynamicExecutor;
    }
}
