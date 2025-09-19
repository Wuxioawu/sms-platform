package com.peng.sms.api.controller;

import com.peng.sms.api.enums.SmsCodeEnum;
import com.peng.sms.api.form.SingleSendForm;
import com.peng.sms.api.uitls.R;
import com.peng.sms.api.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/sms")
public class SmsController {

    @PostMapping(value = "/single_send", produces = "application/json;charset=utf-8")
    public ResultVO singleSend(@RequestBody @Validated SingleSendForm singleSendForm, BindingResult bindingResult) {
        // check the parameter
        if (bindingResult.hasErrors()) {
            String msg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            System.out.println(msg);
            log.error("Invalid parameter: {}", msg);
            return R.error(SmsCodeEnum.PARAMETER_ERROR.getCode(), msg);
        }

        // build the submit ,some check process.


        // send to MQ, convert to strategy modules
        return R.ok();
    }
}
