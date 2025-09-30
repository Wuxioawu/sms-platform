package com.peng.sms.mapper;

import com.peng.sms.entity.SmsUser;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SmsUser record);

    int insertSelective(SmsUser record);

    SmsUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SmsUser record);

    int updateByPrimaryKey(SmsUser record);
}