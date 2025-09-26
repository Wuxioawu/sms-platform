package com.peng.sms.util;

import com.peng.sms.model.StandardReport;

import java.time.LocalDateTime;

public class SearchUtils {
    /**
     * Index prefix
     */
    public static final String INDEX = "sms_submit_log_";

    /**
     * Get the current year
     */
    public static String getYear() {
        return LocalDateTime.now().getYear() + "";
    }

    /**
     * Get the index name for the current year
     */
    public static String getCurrYearIndex() {
        return INDEX + getYear();
    }


    // ThreadLocal operations
    private static final ThreadLocal<StandardReport> reportThreadLocal = new ThreadLocal<>();

    /**
     * Set the StandardReport object in ThreadLocal
     */
    public static void set(StandardReport report) {
        reportThreadLocal.set(report);
    }

    /**
     * Get the StandardReport object from ThreadLocal
     */
    public static StandardReport get() {
        return reportThreadLocal.get();
    }
}
