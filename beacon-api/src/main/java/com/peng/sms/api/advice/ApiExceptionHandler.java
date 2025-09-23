package com.peng.sms.api.advice;

import com.peng.sms.api.uitls.R;
import com.peng.sms.api.vo.ResultVO;
import com.peng.sms.exception.ApiException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResultVO apiException(ApiException ex) {
        return R.error(ex);
    }
}
