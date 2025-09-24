package com.peng.sms.mq;

import com.peng.sms.constant.RabbitMQConstants;
import com.peng.sms.exception.StrategyException;
import com.peng.sms.filter.StrategyFilterContext;
import com.peng.sms.model.StandardSubmit;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class PreSendListener {

    @Autowired
    private StrategyFilterContext strategyFilterContext;

    @RabbitListener(queues = RabbitMQConstants.SMS_PRE_SEND)
    public void listen(StandardSubmit submit, Message message, Channel channel) throws Exception {
        log.info("[Strategy Module - Receive Message] Received the message sent from the Interface Module: submit = {}", submit);

        System.out.println("start:" + LocalDateTime.now());

        try {
            strategyFilterContext.strategy(submit);
            log.info("[Strategy Module - Consumption Completed] manual ack");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (StrategyException e) {
            log.info("[Strategy Module - Consumption Failed] Validation failed, msg = {}", e.getMessage());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } finally {
            System.out.println("end:" + LocalDateTime.now());
        }
    }
}
