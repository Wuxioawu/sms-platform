package com.peng.sms.util;

import com.peng.sms.enums.CMPP2ResultEnums;

import java.util.HashMap;
import java.util.Map;

public class CMPP2ResultUtil {

    private static Map<Integer, String> results = new HashMap<>();

    static {
        CMPP2ResultEnums[] cmpp2ResultEnums = CMPP2ResultEnums.values();
        for (CMPP2ResultEnums cmpp2ResultEnum : cmpp2ResultEnums) {
            results.put(cmpp2ResultEnum.getResult(), cmpp2ResultEnum.getMsg());
        }
    }

    public static String getResultMessage(Integer result) {
        return results.get(result);
    }
}
