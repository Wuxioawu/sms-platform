package com.peng.sms.exception;

import com.peng.sms.enums.ExceptionEnums;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private Integer code;

    public ApiException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public ApiException(ExceptionEnums enums) {
        super(enums.getMsg());
        this.code = enums.getCode();
    }

}
