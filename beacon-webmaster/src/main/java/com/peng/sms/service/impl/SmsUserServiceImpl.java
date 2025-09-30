package com.peng.sms.service.impl;

import com.peng.sms.entity.SmsUser;
import com.peng.sms.entity.SmsUserExample;
import com.peng.sms.mapper.SmsUserMapper;
import com.peng.sms.service.SmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsUserServiceImpl implements SmsUserService {

    @Autowired
    private SmsUserMapper smsUserMapper;

    @Override
    public SmsUser findByUsername(String username) {
        SmsUserExample example = new SmsUserExample();
        SmsUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<SmsUser> list = smsUserMapper.selectByExample(example);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }
}
