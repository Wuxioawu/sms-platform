package com.peng.sms.service;

import com.peng.sms.entity.ClientBusiness;

import java.util.List;

public interface ClientBusinessService {

    List<ClientBusiness> findAll();

    List<ClientBusiness> findByUserId(Integer userId);
}
