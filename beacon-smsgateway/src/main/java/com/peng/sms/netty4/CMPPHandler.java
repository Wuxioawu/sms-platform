package com.peng.sms.netty4;

import com.peng.sms.constant.SmsConstant;
import com.peng.sms.model.StandardReport;
import com.peng.sms.model.StandardSubmit;
import com.peng.sms.netty4.entity.CmppDeliver;
import com.peng.sms.netty4.entity.CmppSubmitResp;
import com.peng.sms.netty4.utils.MsgUtils;
import com.peng.sms.runnable.DeliverRunnable;
import com.peng.sms.runnable.SubmitRepoRunnable;
import com.peng.sms.util.CMPP2ResultUtil;
import com.peng.sms.util.CMPPSubmitRepoMapUtil;
import com.peng.sms.util.SpringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * 主要业务 handler,运营商响应信息
 */
public class CMPPHandler extends SimpleChannelInboundHandler {
    private final static Logger log = LoggerFactory.getLogger(CMPPHandler.class);

    private final Integer OK = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext context, Object msg) throws Exception {

        if (msg instanceof CmppSubmitResp) {
            CmppSubmitResp resp = (CmppSubmitResp) msg;
            log.info("-------------接收到短信提交应答-------------");
            log.info("----自增id：" + resp.getSequenceId());
            log.info("----状态：" + resp.getResult());
            log.info("----第一次响应：" + resp.getMsgId());

            ThreadPoolExecutor cmppSubmitPool = (ThreadPoolExecutor) SpringUtil.getBeanByName("cmppSubmitPool");
            cmppSubmitPool.execute(new SubmitRepoRunnable(resp));
        }

        if (msg instanceof CmppDeliver) {
            CmppDeliver resp = (CmppDeliver) msg;
            // 是否为状态报告 0：非状态报告  1：状态报告
            if (resp.getRegistered_Delivery() == 1) {
                // 如果是状态报告的话
                log.info("-------------状态报告---------------");
                log.info("----第二次响应：" + resp.getMsg_Id_DELIVRD());
                log.info("----手机号：" + resp.getDest_terminal_Id());
                log.info("----状态：" + resp.getStat());

                ThreadPoolExecutor cmppDeliverPool = (ThreadPoolExecutor) SpringUtil.getBeanByName("cmppDeliverPool");
                cmppDeliverPool.execute(new DeliverRunnable(resp.getMsg_Id_DELIVRD(), resp.getStat()));
            } else {
                //用户回复会打印在这里
                log.info("" + MsgUtils.bytesToLong(resp.getMsg_Id()));
                log.info(resp.getSrc_terminal_Id());
                log.info(resp.getMsg_Content());
            }
        }
    }
}
