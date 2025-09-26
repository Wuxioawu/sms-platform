package com.peng.sms.mq;

import com.peng.sms.model.StandardSubmit;
import com.peng.sms.netty4.NettyClient;
import com.peng.sms.netty4.entity.CmppSubmit;
import com.peng.sms.netty4.utils.Command;
import com.peng.sms.netty4.utils.MsgUtils;
import com.peng.sms.util.CMPPSubmitRepoMapUtil;
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

    @Autowired
    private NettyClient nettyClient;

    @RabbitListener(queues = "${gateway.sendtopic}")
    public void consume(StandardSubmit submit, Channel channel, Message message) throws IOException, InterruptedException {
        log.info("[SMS Gateway Module] Received message submit = {}", submit);
        // =====================Complete interaction with the carrier: send one request, receive two responses==========================
        // 1. This sequence is implemented using increment (++). When the value reaches MAX, it resets,
        //    so the value can be reused.
        int sequence = MsgUtils.getSequence();

        CmppSubmit cmppSubmit = new CmppSubmit(
                Command.CMPP2_VERSION,
                submit.getSrcNumber(),
                sequence,
                submit.getMobile(),
                submit.getText()
        );
        // 3. Temporarily store the submit object, so it can be retrieved when the carrier responds for the first time.
        //    If you worry about issues like server crashes and data loss, you can store it in Redis instead.
        CMPPSubmitRepoMapUtil.put(sequence, submit);

        nettyClient.submit(cmppSubmit);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
