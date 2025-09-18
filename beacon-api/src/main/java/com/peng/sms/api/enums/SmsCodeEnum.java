package com.peng.sms.api.enums;

import lombok.Getter;

@Getter
public enum SmsCodeEnum {

    PARAMETER_ERROR(-10, "Invalid parameter");

    private final Integer code;
    private final String msg;

    SmsCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
