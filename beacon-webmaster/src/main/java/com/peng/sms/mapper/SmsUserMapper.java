package com.peng.sms.mapper;

import com.peng.sms.entity.SmsUser;
import com.peng.sms.entity.SmsUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsUserMapper {
    long countByExample(SmsUserExample example);

    int deleteByExample(SmsUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SmsUser record);

    int insertSelective(SmsUser record);

    List<SmsUser> selectByExample(SmsUserExample example);

    SmsUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SmsUser record, @Param("example") SmsUserExample example);

    int updateByExample(@Param("record") SmsUser record, @Param("example") SmsUserExample example);

    int updateByPrimaryKeySelective(SmsUser record);

    int updateByPrimaryKey(SmsUser record);
}