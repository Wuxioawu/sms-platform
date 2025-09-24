package com.peng.sms.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitTemplateConfig {
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        // 1. Build a RabbitTemplate object
        RabbitTemplate rabbitTemplate = new RabbitTemplate();

        // 2. Set the connectionFactory
        rabbitTemplate.setConnectionFactory(connectionFactory);

        // 3. Configure the confirm mechanism
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {

            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                // If ack is false, the message was not sent to the exchange
                if (!ack) {
                    log.error("【Interface Module - Send Message】 Message was not sent to the exchange, correlationData = {}, cause = {}", correlationData, cause);
                }
            }
        });

        // 4. Configure the return mechanism
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {

            // This callback is triggered when the exchange could not route the message to the specified queue
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.error("【Interface Module - Send Message】 Message was not routed to the specified Queue. message = {}, exchange = {}, routingKey = {}",
                        new String(message.getBody()), exchange, routingKey);
            }
        });

        // 5. Return the configured RabbitTemplate
        return rabbitTemplate;
    }
}
