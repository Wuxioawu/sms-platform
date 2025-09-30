package com.peng.sms.controller;

import com.peng.sms.constant.WebMasterConstants;
import com.peng.sms.dto.UserDTO;
import com.peng.sms.entity.SmsUser;
import com.peng.sms.enums.ExceptionEnums;
import com.peng.sms.service.SmsMenuService;
import com.peng.sms.util.R;
import com.peng.sms.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys")
@Slf4j
public class SmsUserController {

    @Autowired
    private SmsMenuService menuService;

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

    @GetMapping("/user/info")
    public ResultVO info() {

        Subject subject = SecurityUtils.getSubject();
        SmsUser smsUser = (SmsUser) subject.getPrincipal();
        if (smsUser == null) {
            log.info("【Get logged-in user info】 User is not logged in!!");
            return R.error(ExceptionEnums.NOT_LOGIN);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("nickname", smsUser.getNickname());
        data.put("username", smsUser.getUsername());
        return R.ok(data);
    }

    @GetMapping("/menu/user")
    public ResultVO menuUser() {
        SmsUser smsUser = (SmsUser) SecurityUtils.getSubject().getPrincipal();
        if (smsUser == null) {
            log.info("【Get user menu info】 User is not logged in!!");
            return R.error(ExceptionEnums.NOT_LOGIN);
        }

        List<Map< String, Object>> data = menuService.findUserMenu(smsUser.getId());
        if (data == null) {
            log.error("【Get user menu info】 Failed to fetch user menu!! id = {}", smsUser.getId());
            return R.error(ExceptionEnums.USER_MENU_ERROR);
        }
        return R.ok(data);
    }

}
