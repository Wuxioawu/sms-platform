package com.peng.sms.api.controller;

import com.peng.sms.api.enums.SmsCodeEnum;
import com.peng.sms.api.filter.CheckFilterContext;
import com.peng.sms.api.form.SingleSendForm;
import com.peng.sms.api.uitls.R;
import com.peng.sms.api.vo.ResultVO;
import com.peng.sms.constant.RabbitMQConstants;
import com.peng.sms.model.StandardSubmit;
import com.peng.sms.util.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/sms")
@RefreshScope
public class SmsController {

    private final String LogName = "SmsController";

    @Value("${headers}")
    private String headers;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CheckFilterContext checkFilterContext;

    @Autowired
    private SnowFlakeUtil snowFlakeUtil;

    private final String UNKNOW = "unknow";
    private final String X_FORWARDED_FOR = "x-forwarded-for";
    private final String UTC = "+8";

    @PostMapping(value = "/single_send", produces = "application/json;charset=utf-8")
    public ResultVO singleSend(@RequestBody @Validated SingleSendForm singleSendForm, BindingResult bindingResult, HttpServletRequest request) {
        // check the parameter
        if (bindingResult.hasErrors()) {
            String msg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            System.out.println(msg);
            log.error("Invalid parameter: {}", msg);
            return R.error(SmsCodeEnum.PARAMETER_ERROR.getCode(), msg);
        }

        String ip = getRealIP(request);

        // build the submit ,some check process.
        StandardSubmit submit = new StandardSubmit();
        submit.setRealIP(ip);
        submit.setApikey(singleSendForm.getAipKey());
        submit.setMobile(singleSendForm.getMobileNumber());
        submit.setText(singleSendForm.getMessageContent());
        submit.setState(singleSendForm.getMessageType());
        submit.setUid(singleSendForm.getUid());

        // check process
        checkFilterContext.check(submit);
        // base ont the snow
        submit.setSequenceId(snowFlakeUtil.nextId());
        submit.setSendTime(System.currentTimeMillis());

        // send to MQ, convert to strategy modules
        rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_PRE_SEND, submit, new CorrelationData(submit.getSequenceId().toString()));
        return R.ok();
    }

    private String getRealIP(HttpServletRequest req) {
        //1. state the ip
        String ip;

        //2. check the head, and get the ip address by req
        for (String header : headers.split(",")) {

            if (StringUtils.isEmpty(header)) {
                log.info("{}: getRealIP: the header is empty", LogName);
                continue;
            }

            ip = req.getHeader(header);
            if (StringUtils.isEmpty(ip) || UNKNOW.equalsIgnoreCase(ip)) {
                log.info("{}: getRealIP: req.getHeader(header) is empty}", LogName);
                continue;
            }

            if (X_FORWARDED_FOR.equalsIgnoreCase(header) && ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }

            return ip;
        }
        //if can not get the ip by request head
        return req.getRemoteAddr();
    }
}


