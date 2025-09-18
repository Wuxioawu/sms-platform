package com.peng.sms.api.controller;

import com.peng.sms.api.form.SingleSendForm;
import com.peng.sms.api.uitls.R;
import com.peng.sms.api.vo.ResultVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @PostMapping(value = "/single_send",produces = "application/json;charset=utf-8")
    public ResultVO singleSend(@RequestBody SingleSendForm singleSendForm) {
        return R.ok();
    }
}
