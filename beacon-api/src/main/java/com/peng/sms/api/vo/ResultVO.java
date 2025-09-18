package com.peng.sms.api.vo;

import lombok.Data;

@Data
public class ResultVO {
    /**
     * 0 indicates successful reception; other codes indicate errors
     */
    private Integer code;
    /**
     * Message describing the status
     */
    private String msg;
    /**
     * Number of SMS segments (one segment is 70 characters; if exceeding 70, each segment is 67 characters)
     */
    private Integer count;
    /**
     * Amount deducted, in li (1/1000 RMB)
     */
    private Long fee;
    /**
     * UID provided by the client request
     */
    private String uid;
    /**
     * SMS ID within the platform, 64-bit integer
     */
    private String sid;
}
