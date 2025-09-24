package com.peng.sms.mq;

import com.peng.sms.constant.RabbitMQConstants;
import com.peng.sms.model.StandardSubmit;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PreSendListener {

    @RabbitListener(queues = RabbitMQConstants.SMS_PRE_SEND)
    public void listen(StandardSubmit submit, Message message, Channel channel ) throws Exception {
        log.info("[Strategy Module - Receive Message] Received the message sent from the Interface Module: submit = {}", submit);
        // deal with the strategy
        log.info("[Strategy Module - Consumption Completed] manual ack");
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
