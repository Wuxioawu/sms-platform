package com.peng.sms.api.config;

import com.peng.sms.constant.RabbitMQConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue preSendQueue() {
        return QueueBuilder.durable(RabbitMQConstants.SMS_PRE_SEND).build();
    }
}
