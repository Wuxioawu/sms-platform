package com.peng.sms.controller;

import com.peng.sms.constant.WebMasterConstants;
import com.peng.sms.dto.UserDTO;
import com.peng.sms.enums.ExceptionEnums;
import com.peng.sms.util.R;
import com.peng.sms.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/sys")
@Slf4j
public class SmsUserController {

    @PostMapping("/login")
    public ResultVO login(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("【Authentication Operation】Invalid parameters, userDTO = {}", userDTO);
            return R.error(ExceptionEnums.PARAMETER_ERROR);
        }

        String realKaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute(WebMasterConstants.KAPTCHA);
        if (!userDTO.getCaptcha().equalsIgnoreCase(realKaptcha)) {
            log.info("【Authentication Operation】Incorrect captcha, captcha = {}, realKaptcha = {}", userDTO.getCaptcha(), realKaptcha);
            return R.error(ExceptionEnums.KAPACHA_ERROR);
        }

        UsernamePasswordToken token = new UsernamePasswordToken(userDTO.getUsername(), userDTO.getPassword());
        token.setRememberMe(userDTO.getRememberMe());

        try {
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e) {
            log.info("【Authentication Operation】Invalid username or password, ex = {}", e.getMessage());
            return R.error(ExceptionEnums.AUTHED_ERROR);
        }

        return R.ok();
    }
}
