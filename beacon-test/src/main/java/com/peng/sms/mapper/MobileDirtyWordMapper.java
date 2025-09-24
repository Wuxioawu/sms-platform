package com.peng.sms.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MobileDirtyWordMapper {
    @Select("select dirtyword from mobile_dirtyword")
    List<String> findDirtyWord();
}
