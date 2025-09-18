package com.peng.sms.api.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class SingleSendForm {

    @NotBlank(message = "The apikey cannot be empty!")
    private String aipKey;
    @NotBlank(message = "The mobileNumber cannot be empty!")
    private String mobileNumber;
    @NotBlank(message = "The messageContent cannot be empty!")
    private String messageContent;
    /**
     * ID in your business system; will be carried back in callbacks
     */
    private String uid;
    @Range(min = 0, max = 2, message = "the messageType must be from 0 to 2")
    @NotNull(message = "The messageType cannot be empty")
    private Integer messageType;
}
