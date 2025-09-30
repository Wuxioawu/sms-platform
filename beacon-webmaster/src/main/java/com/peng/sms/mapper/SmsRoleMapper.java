package com.peng.sms.mapper;

import com.peng.sms.entity.SmsRole;
import com.peng.sms.entity.SmsRoleExample;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

public interface SmsRoleMapper {
    long countByExample(SmsRoleExample example);

    int deleteByExample(SmsRoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SmsRole record);

    int insertSelective(SmsRole record);

    List<SmsRole> selectByExample(SmsRoleExample example);

    SmsRole selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SmsRole record, @Param("example") SmsRoleExample example);

    int updateByExample(@Param("record") SmsRole record, @Param("example") SmsRoleExample example);

    int updateByPrimaryKeySelective(SmsRole record);

    int updateByPrimaryKey(SmsRole record);

    @Select("select \n" +
            "\tname\n" +
            "from \n" +
            "\tsms_role sr\n" +
            "inner join \n" +
            "\tsms_user_role sur\n" +
            "on\n" +
            "\tsr.id = sur.user_id\n" +
            "where \n" +
            "  sur.user_id = #{userId}")
    Set<String> findRoleNameByUserId(@Param("userId") Integer userId);
}