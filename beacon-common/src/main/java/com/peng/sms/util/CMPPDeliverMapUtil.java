package com.peng.sms.util;

import com.peng.sms.model.StandardReport;

import java.util.concurrent.ConcurrentHashMap;

public class CMPPDeliverMapUtil {

    private static ConcurrentHashMap<String, StandardReport> map = new ConcurrentHashMap<>();

    public static void put(String msgId, StandardReport submit) {
        map.put(msgId, submit);
    }

    public static StandardReport get(String msgId) {
        return map.get(msgId);
    }

    public static StandardReport remove(String msgId) {
        return map.remove(msgId);
    }
}
