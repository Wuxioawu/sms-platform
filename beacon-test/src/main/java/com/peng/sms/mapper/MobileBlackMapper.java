package com.peng.sms.mapper;

import com.peng.sms.entity.MobileBlack;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MobileBlackMapper {
    @Select("select black_number,client_id from mobile_black where is_delete = 0")
    List<MobileBlack> findAll();
}
