package com.peng.sms.task;

import com.peng.sms.client.CacheClient;
import com.peng.sms.constant.RabbitMQConstants;
import com.peng.sms.util.MailUtil;
import com.rabbitmq.client.Channel;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Set;

@Component
@Slf4j
public class MonitorQueueMessageCountTask {

    // Email template
    String text = "<h1> Your queue has a message backlog. Queue name: %s, Message count: %s </h1>";

    // Queue message limit
    private final long MESSAGE_COUNT_LIMIT = 10;

    // Pattern to query queue names
    private final String QUEUE_PATTERN = "channel:*";

    // Index to extract channelID
    private final int CHANNEL_ID_INDEX = QUEUE_PATTERN.indexOf("*");

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private MailUtil mailUtil;

    @XxlJob("monitorQueueMessageCountTask")
    public void monitor() throws MessagingException, IOException {
        Set<String> keys = cacheClient.keys(QUEUE_PATTERN);

        Channel channel = connectionFactory.createConnection().createChannel(false);

        // Check a specific queue
        listenQueueAndSendEmail(channel, RabbitMQConstants.SMS_PRE_SEND);

        // Check all queues based on keys
        for (String key : keys) {
            String queueName = RabbitMQConstants.SMS_GATEWAY + key.substring(CHANNEL_ID_INDEX);
            listenQueueAndSendEmail(channel, queueName);
        }
    }

    // Check queue and send email if message count exceeds limit
    private void listenQueueAndSendEmail(Channel channel, String queueName) throws MessagingException, IOException {
        // Declare queue if it does not exist
        channel.queueDeclare(queueName, true, false, false, null);
        long count = 0;
        count = channel.messageCount(queueName);
        if (count > MESSAGE_COUNT_LIMIT) {
            mailUtil.sendEmail(queueName + " queue has a backlog", String.format(text, queueName, count));
        }
    }
}
