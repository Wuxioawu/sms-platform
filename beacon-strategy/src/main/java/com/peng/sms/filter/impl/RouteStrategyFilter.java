package com.peng.sms.filter.impl;

import com.peng.sms.client.BeaconCacheClient;
import com.peng.sms.constant.CacheConstant;
import com.peng.sms.constant.RabbitMQConstants;
import com.peng.sms.enums.ExceptionEnums;
import com.peng.sms.exception.StrategyException;
import com.peng.sms.filter.StrategyFilter;
import com.peng.sms.model.StandardSubmit;
import com.peng.sms.uitl.ChannelTransferUtil;
import com.peng.sms.uitl.ErrorSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import static com.peng.sms.filter.impl.RouteStrategyFilter.ChannelConstants.*;

@Service(value = "route")
@Slf4j
public class RouteStrategyFilter implements StrategyFilter {

    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private ErrorSendMsgUtil sendMsgUtil;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    static class ChannelConstants {
        static final String CLIENT_CHANNEL_WEIGHT = "clientChannelWeight";

        static final String CHANNEL_ID = "channelId";

        static final String IS_AVAILABLE = "isAvailable";

        static final String CHANNEL_TYPE = "channelType";

        static final String CHANNEL_NUMBER = "channelNumber";

        static final String CHANNEL_CLIENT_NUMBER = "clientChannelNumber";

        static final String ID = "id";

    }

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("[Strategy Module - RouteStrategyFilter] running ");

        Long clientId = submit.getClientId();

        Set<Map> channels = cacheClient.smemberMap(CacheConstant.CLIENT_CHANNEL + clientId);

        // 3. Sort the client channels by weight (higher weight first)
        TreeSet<Map> clientWeightChannels = new TreeSet<>((o1, o2) ->
                Integer.parseInt(o2.get(CLIENT_CHANNEL_WEIGHT) + "") - Integer.parseInt(o1.get(CLIENT_CHANNEL_WEIGHT) + ""));

        clientWeightChannels.addAll(channels);
        // 4. Select the channel with higher weight

        boolean ok = false;
        Map channel = null;
        Map clientChannel = null;

        for (Map clientWeightChannel : clientWeightChannels) {
            // 5. If the client-channel binding is not available, skip
            if ((int) (clientWeightChannel.get(IS_AVAILABLE)) != 0) {
                continue;
            }

            // 6. Retrieve the channel info from Redis and check availability and operator compatibility
            channel = cacheClient.hGetAll(CacheConstant.CHANNEL + clientWeightChannel.get(CHANNEL_ID));
            if ((int) (channel.get(IS_AVAILABLE)) != 0) {
                continue;
            }
            // Get channel communication type
            Integer channelType = (Integer) channel.get(CHANNEL_TYPE);
            if (channelType != 0 && !Objects.equals(submit.getOperatorId(), channelType)) {
                // Channel is not universal, and operator does not match
                continue;
            }

            // 7. Placeholder for future channel transfer logic
            Map transferChannel = ChannelTransferUtil.transfer(submit, channel);

            // Found an available channel
            ok = true;
            clientChannel = clientWeightChannel;
            break;
        }

        if (!ok) {
            log.info("【Strategy Module - Routing Strategy】  No available channel selected!");
            sendMsgUtil.sendErrorMessage(submit, ExceptionEnums.NO_CHANNEL, ExceptionEnums.NO_CHANNEL.getMsg());
        }

        // 8. Populate submit with the selected channel info
        submit.setChannelId(Long.parseLong(channel.get(ID) + ""));
        submit.setSrcNumber("" + channel.get(CHANNEL_NUMBER) + clientChannel.get(CHANNEL_CLIENT_NUMBER));

        try {
            // 9. Declare the queue name and create the queue
            String queueName = RabbitMQConstants.SMS_GATEWAY + submit.getChannelId();
            amqpAdmin.declareQueue(QueueBuilder.durable(queueName).build());
            // 10. Send the message to the declared queue
            rabbitTemplate.convertAndSend(queueName, submit);
            log.info("【Strategy Module - Routing Strategy】  sending message success!!!");
        } catch (AmqpException e) {
            log.info("【Strategy Module - Routing Strategy】  Error occurred while declaring queue or sending message!");
            sendMsgUtil.sendErrorMessage(submit, ExceptionEnums.UNKNOWN_ERROR, e.getMessage());
        }
    }
}
