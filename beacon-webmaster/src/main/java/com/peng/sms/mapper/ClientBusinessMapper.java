package com.peng.sms.mapper;

import com.peng.sms.entity.ClientBusiness;
import com.peng.sms.entity.ClientBusinessExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientBusinessMapper {
    long countByExample(ClientBusinessExample example);

    int deleteByExample(ClientBusinessExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ClientBusiness record);

    int insertSelective(ClientBusiness record);

    List<ClientBusiness> selectByExample(ClientBusinessExample example);

    ClientBusiness selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ClientBusiness record, @Param("example") ClientBusinessExample example);

    int updateByExample(@Param("record") ClientBusiness record, @Param("example") ClientBusinessExample example);

    int updateByPrimaryKeySelective(ClientBusiness record);

    int updateByPrimaryKey(ClientBusiness record);
}