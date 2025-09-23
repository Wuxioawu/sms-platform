package com.peng.sms.enums;

import lombok.Getter;

@Getter
public enum ExceptionEnums {
    UNKNOWN_ERROR(-999, "Unknown error!"),
    ERROR_APIKEY(-1, "Invalid API key"),
    IP_NOT_WHITE(-2, "Request IP is not in the whitelist"),
    ERROR_SIGN(-3, "No available signature"),
    ERROR_TEMPLATE(-4, "No available template"),
    ERROR_MOBILE(-5, "Invalid mobile number format"),
    BALANCE_NOT_ENOUGH(-6, "Insufficient customer balance"),
    PARAMETER_ERROR(-10, "Invalid parameter!"),
    SNOWFLAKE_OUT_OF_RANGE(-11, "Snowflake machine ID or service ID exceeds maximum range!"),
    SNOWFLAKE_TIME_BACK(-12, "Snowflake algorithm time rollback issue detected!"),
    HAVE_DIRTY_WORD(-13, "Message content contains sensitive words!"),
    BLACK_GLOBAL(-14, "The mobile number is in the global blacklist!"),
    BLACK_CLIENT(-15, "The mobile number is in the client blacklist!"),
    ONE_MINUTE_LIMIT(-16, "1-minute rate limit rule triggered, SMS cannot be sent"),
    ONE_HOUR_LIMIT(-17, "1-hour rate limit rule triggered, SMS cannot be sent"),
    NO_CHANNEL(-18, "No suitable channel selected!"),
    SEARCH_INDEX_ERROR(-19, "Failed to add document information!"),
    SEARCH_UPDATE_ERROR(-20, "Failed to update document information!"),

    KAPACHA_ERROR(-100, "Verification code error!"),
    AUTHED_ERROR(-101, "Incorrect username or password!"),
    NOT_LOGIN(-102, "User not logged in!"),
    USER_MENU_ERROR(-103, "Failed to retrieve user menu information!"),
    SMS_NO_AUTHOR(-104, "Current user does not have permission to query this SMS information");

    private final Integer code;
    private final String msg;

    ExceptionEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
