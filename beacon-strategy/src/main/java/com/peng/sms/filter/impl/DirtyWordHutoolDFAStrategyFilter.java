package com.peng.sms.filter.impl;

import com.peng.sms.client.BeaconCacheClient;
import com.peng.sms.enums.ExceptionEnums;
import com.peng.sms.exception.StrategyException;
import com.peng.sms.filter.StrategyFilter;
import com.peng.sms.model.StandardSubmit;
import com.peng.sms.uitl.ErrorSendMsgUtil;
import com.peng.sms.uitl.HutoolDFAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "hutoolDFADirtyWord")
@Slf4j
public class DirtyWordHutoolDFAStrategyFilter implements StrategyFilter {

    @Autowired
    private ErrorSendMsgUtil sendMsgUtil;

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【Strategy Module - Sensitive Word Check】 Validating...");
        String text = submit.getText();

        List<String> dirtyWords = HutoolDFAUtil.getDirtyWord(text);

        if (dirtyWords != null && !dirtyWords.isEmpty()) {
            log.info("【Strategy Module - Sensitive Word Check】 SMS content contains sensitive words, dirtyWords = {}", dirtyWords);

            // ================================Send write-log message================================
            submit.setErrorMsg(ExceptionEnums.HAVE_DIRTY_WORD.getMsg() + "dirtyWords = " + dirtyWords);
            sendMsgUtil.sendWriteLog(submit);
            // ================================Send status report message================================
            sendMsgUtil.sendPushReport(submit);
            // // ================================Throw exception================================
            throw new StrategyException(ExceptionEnums.HAVE_DIRTY_WORD);
        }
        log.info("【Strategy Module - Sensitive Word Check】 Validation passed, no sensitive words found");
    }
}
