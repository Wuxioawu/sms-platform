package com.peng.sms.service.impl;

import com.peng.sms.entity.ClientBusiness;
import com.peng.sms.entity.ClientBusinessExample;
import com.peng.sms.mapper.ClientBusinessMapper;
import com.peng.sms.service.ClientBusinessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ClientBusinessServiceImpl implements ClientBusinessService {
    @Resource
    private ClientBusinessMapper clientBusinessMapper;

    @Override
    public List<ClientBusiness> findAll() {
        List<ClientBusiness> list = clientBusinessMapper.selectByExample(null);
        return list;
    }

    @Override
    public List<ClientBusiness> findByUserId(Integer userId) {
        ClientBusinessExample example = new ClientBusinessExample();
        example.createCriteria().andExtend1EqualTo(userId + "");
        List<ClientBusiness> list = clientBusinessMapper.selectByExample(example);
        return list;
    }
}
