package com.peng.sms.util;

import com.peng.sms.enums.MobileOperatorEnum;

import java.util.HashMap;
import java.util.Map;

public class OperatorUtil {

    private static Map<String, Integer> operators = new HashMap<>();

    static {
        MobileOperatorEnum[] operatorEnums = MobileOperatorEnum.values();
        for (MobileOperatorEnum operatorEnum : operatorEnums) {
            operators.put(operatorEnum.getOperatorName(), operatorEnum.getOperatorId());
        }
    }

    public static Integer getOperatorIdByOperatorName(String operatorName) {
        return operators.get(operatorName);
    }

}
