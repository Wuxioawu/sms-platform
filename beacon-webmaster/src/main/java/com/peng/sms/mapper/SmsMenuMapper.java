package com.peng.sms.mapper;

import com.peng.sms.entity.SmsMenu;
import com.peng.sms.entity.SmsMenuExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsMenuMapper {
    long countByExample(SmsMenuExample example);

    int deleteByExample(SmsMenuExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SmsMenu record);

    int insertSelective(SmsMenu record);

    List<SmsMenu> selectByExample(SmsMenuExample example);

    SmsMenu selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SmsMenu record, @Param("example") SmsMenuExample example);

    int updateByExample(@Param("record") SmsMenu record, @Param("example") SmsMenuExample example);

    int updateByPrimaryKeySelective(SmsMenu record);

    int updateByPrimaryKey(SmsMenu record);

    List<Map<String, Object>> findMenuByUserId(@Param("userId") Integer id);
}