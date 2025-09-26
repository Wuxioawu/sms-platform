package com.peng.sms.util;

import com.peng.sms.enums.CMPP2DeliverEnums;

import java.util.HashMap;
import java.util.Map;

public class CMPP2DeliverUtil {

    private static Map<String, String> stats = new HashMap<>();

    static {
        CMPP2DeliverEnums[] cmpp2DeliverEnums = CMPP2DeliverEnums.values();
        for (CMPP2DeliverEnums cmpp2DeliverEnum : cmpp2DeliverEnums) {
            stats.put(cmpp2DeliverEnum.getStat(), cmpp2DeliverEnum.getDescription());
        }
    }

    public static String getResultMessage(String stat) {
        return stats.get(stat);
    }
}
