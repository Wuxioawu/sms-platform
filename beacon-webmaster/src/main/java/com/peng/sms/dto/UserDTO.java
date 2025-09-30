package com.peng.sms.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String captcha;

    private Boolean rememberMe = false;


}
