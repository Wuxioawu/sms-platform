package com.peng.sms.mq;

import com.peng.sms.model.StandardSubmit;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class SmsGatewayListener {



    @RabbitListener(queues = "${gateway.sendtopic}")
    public void consume(StandardSubmit submit, Channel channel, Message message) throws IOException, InterruptedException {
        log.info("[SMS Gateway Module] Received message submit = {}", submit);
        // =====================Complete interaction with the carrier: send one request, receive two responses==========================
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
