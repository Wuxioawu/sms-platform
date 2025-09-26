package com.peng.sms.exception;

import com.peng.sms.enums.ExceptionEnums;
import lombok.Getter;

@Getter
public class SearchException extends RuntimeException {

    private final Integer code;

    public SearchException(String message, Integer code) {
        super(message);
        this.code = code;
    }


    public SearchException(ExceptionEnums enums) {
        super(enums.getMsg());
        this.code = enums.getCode();
    }

}
