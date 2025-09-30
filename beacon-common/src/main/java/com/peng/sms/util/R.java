package com.peng.sms.util;

import com.peng.sms.enums.ExceptionEnums;
import com.peng.sms.vo.ResultVO;

public class R {

    public static ResultVO ok() {
        return new ResultVO(0, "");
    }

    public static ResultVO ok(Object data) {
        ResultVO vo = ok();
        vo.setData(data);
        return vo;
    }

    public static ResultVO ok(Long total, Object rows) {
        ResultVO vo = ok();
        vo.setTotal(total);
        vo.setRows(rows);
        return vo;
    }

    public static ResultVO error(ExceptionEnums enums) {
        return new ResultVO(enums.getCode(), enums.getMsg());
    }

}
