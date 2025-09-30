package com.peng.sms.service;

import com.peng.sms.entity.SmsUser;

public interface SmsUserService {
    SmsUser findByUsername(String username);
}
