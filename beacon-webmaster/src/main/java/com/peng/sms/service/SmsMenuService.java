package com.peng.sms.service;

import java.util.List;
import java.util.Map;


public interface SmsMenuService {
    List<Map<String, Object>> findUserMenu(Integer id);
}
