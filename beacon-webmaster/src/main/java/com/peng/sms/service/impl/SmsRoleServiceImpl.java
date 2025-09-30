package com.peng.sms.service.impl;

import com.peng.sms.mapper.SmsRoleMapper;
import com.peng.sms.service.SmsRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Service
public class SmsRoleServiceImpl implements SmsRoleService {
    @Resource
    private SmsRoleMapper roleMapper;

    @Override
    public Set<String> getRoleName(Integer userId) {
        return roleMapper.findRoleNameByUserId(userId);
    }
}
