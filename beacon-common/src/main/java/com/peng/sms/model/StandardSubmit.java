package com.peng.sms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * POJO class used for validation and encapsulation in the Interface Module,
 * Strategy Module, and SMS Gateway Module.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardSubmit {

    /**
     * Unique identifier for the current SMS, generated using the Snowflake algorithm.
     * another function is to bed stored in elasticsearch, be used as the key id
     */
    private Long sequenceId;

    /**
     * Client ID, retrieved from the cache module based on the apikey.
     */
    private Long clientId;

    /**
     * IP whitelist for the client, retrieved from the cache.
     */
    private List<String> ip;

    /**
     * UID from the client's business system, passed in the request.
     */
    private String uid;

    /**
     * Target mobile number, passed in the request.
     */
    private String mobile;

    /**
     * SMS signature in the content, passed in the request.
     * The signature is extracted from the SMS content enclosed in 【】.
     */
    private String sign;

    /**
     * SMS content, passed in the request.
     */
    private String text;

    /**
     * SMS sending time, set to the current system time.
     */
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime sendTime;

    /**
     * Cost of the current SMS.
     * Calculated based on content length: 70 characters per message;
     * if exceeded, 67 characters per additional message.
     * Unit: Li (1/1000 RMB).
     */
    private Long fee;

    /**
     * Operator ID for the target mobile number (Strategy module).
     */
    private Integer operatorId;

    /**
     * Area code for the target mobile number, e.g., 0451, 0455
     * (Strategy module; ignored if third-party lookup not available).
     */
    private Integer areaCode;

    /**
     * Region of the target mobile number, e.g., Harbin, Suihua (Strategy module).
     */
    private String area;

    /**
     * Source number assigned by the SMS channel, e.g., 106934985673485645 (Strategy module).
     */
    private String srcNumber;

    /**
     * Channel ID information (Strategy module).
     */
    private Long channelId;

    /**
     * SMS sending status:
     * 0 - Pending / Sending,
     * 1 - Success,
     * 2 - Failure.
     * Default is 0.
     */
    private int reportState;

    /**
     * Reason for SMS sending failure, recorded in this attribute.
     */
    private String errorMsg;


    /**
     * Actual IP address of the client.
     */
    private String realIP;

    /**
     * API key provided in the client's request.
     */
    private String apikey;

    /**
     * SMS type:
     * 0 - Verification Code SMS,
     * 1 - Notification SMS,
     * 2 - Marketing SMS.
     */
    private int state;

    /**
     * ID of the SMS signature.
     */
    private Long signId;

    /**
     * Whether number portability check is performed:
     * isTransfer = true means the check and handling have been performed.
     */
    private Boolean isTransfer = false;

    /**
     * System timestamp (in milliseconds) stored for the 1-hour rate-limiting rule.
     */
    private Long oneHourLimitMilli;
}
