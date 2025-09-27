package com.peng.sms.runnable;

import com.peng.sms.constant.RabbitMQConstants;
import com.peng.sms.constant.SmsConstant;
import com.peng.sms.model.StandardReport;
import com.peng.sms.model.StandardSubmit;
import com.peng.sms.netty4.entity.CmppSubmitResp;
import com.peng.sms.util.CMPP2ResultUtil;
import com.peng.sms.util.CMPPDeliverMapUtil;
import com.peng.sms.util.CMPPSubmitRepoMapUtil;
import com.peng.sms.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;

@Slf4j
public class SubmitRepoRunnable implements Runnable {

    private RabbitTemplate rabbitTemplate = SpringUtil.getBeanByClass(RabbitTemplate.class);

    private CmppSubmitResp submitResp;

    private final int OK = 0;

    public SubmitRepoRunnable(CmppSubmitResp submitResp) {
        this.submitResp = submitResp;
    }

    @Override
    public void run() {

        StandardReport report;
        StandardSubmit submit = CMPPSubmitRepoMapUtil.remove(submitResp.getSequenceId());

        int result = submitResp.getResult();
        if (result != OK) {
            String resultMessage = CMPP2ResultUtil.getResultMessage(result);
            submit.setReportState(SmsConstant.REPORT_FAIL);
            submit.setErrorMsg(resultMessage);
        } else {
            log.info("[SMS Gateway Module] -> SubmitRepoRunnable: send message successfully");
            report = new StandardReport();
            BeanUtils.copyProperties(submit, report);
            CMPPDeliverMapUtil.put(submitResp.getMsgId() + "", report);
        }
        rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_WRITE_LOG, submit);
    }
}
