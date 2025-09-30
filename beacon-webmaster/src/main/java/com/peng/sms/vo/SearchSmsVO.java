package com.peng.sms.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchSmsVO {
    // ok - company name
    private String corpname;

    // format TBD (to be determined)
    private String sendTimeStr;

    // page to be modified
    private Integer reportState;

    // change "all network" to "unknown"
    private Integer operatorId;

    // original errorCode
    private String errorMsg;

    // ok - source number
    private String srcNumber;

    // ok - mobile number
    private String mobile;

    // ok - message text
    private String text;

}
