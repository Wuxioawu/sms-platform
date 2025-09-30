package com.peng.sms.mapper;

import com.peng.sms.entity.SmsMenu;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsMenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SmsMenu record);

    int insertSelective(SmsMenu record);

    SmsMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SmsMenu record);

    int updateByPrimaryKey(SmsMenu record);
}