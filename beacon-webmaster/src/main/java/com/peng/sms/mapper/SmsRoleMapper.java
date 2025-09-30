package com.peng.sms.mapper;

import com.peng.sms.entity.SmsRole;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SmsRole record);

    int insertSelective(SmsRole record);

    SmsRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SmsRole record);

    int updateByPrimaryKey(SmsRole record);
}