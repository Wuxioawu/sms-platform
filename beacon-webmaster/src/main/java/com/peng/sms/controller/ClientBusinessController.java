package com.peng.sms.controller;

import com.peng.sms.constant.WebMasterConstants;
import com.peng.sms.entity.ClientBusiness;
import com.peng.sms.entity.SmsUser;
import com.peng.sms.enums.ExceptionEnums;
import com.peng.sms.service.ClientBusinessService;
import com.peng.sms.service.SmsRoleService;
import com.peng.sms.util.R;
import com.peng.sms.vo.ClientBusinessVO;
import com.peng.sms.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
public class ClientBusinessController {
    @Autowired
    private SmsRoleService roleService;

    @Autowired
    private ClientBusinessService clientBusinessService;


    @GetMapping("/sys/clientbusiness/all")
    public ResultVO all() {
        // 1. Get information of the currently logged-in user
        SmsUser smsUser = (SmsUser) SecurityUtils.getSubject().getPrincipal();
        if (smsUser == null) {
            log.info("[Get Client Information] User not logged in!!");
            return R.error(ExceptionEnums.NOT_LOGIN);
        }
        Integer userId = smsUser.getId();
        // 2. Query the current user's role information
        Set<String> roleNameSet = roleService.getRoleName(userId);

        // 3. Fetch data according to the role information
        List<ClientBusiness> list = null;
        if (roleNameSet != null && roleNameSet.contains(WebMasterConstants.ROOT)) {
            list = clientBusinessService.findAll();
        } else {
            list = clientBusinessService.findByUserId(userId);
        }

        List<ClientBusinessVO> data = new ArrayList<>();
        for (ClientBusiness clientBusiness : list) {
            ClientBusinessVO vo = new ClientBusinessVO();
            BeanUtils.copyProperties(clientBusiness, vo);
            data.add(vo);
        }
        return R.ok(data);
    }
}
