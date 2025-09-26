package com.peng.sms.mq;

import com.peng.sms.constant.RabbitMQConstants;
import com.peng.sms.model.StandardSubmit;
import com.peng.sms.service.SearchService;
import com.peng.sms.util.JsonUtil;
import com.peng.sms.util.SearchUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class SmsWriteLogListener {

    @Autowired
    private SearchService searchService;

    @RabbitListener(queues = RabbitMQConstants.SMS_WRITE_LOG)
    public void consume(StandardSubmit submit, Channel channel, Message message) throws IOException {
        // Invoke the add method of the search module to perform the add operation
        log.info("[search module] -> Received log information, submit = {}", submit);

        searchService.index(SearchUtils.INDEX + SearchUtils.getYear(), submit.getSequenceId().toString(), JsonUtil.obj2JSON(submit));
        //2、manual ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
