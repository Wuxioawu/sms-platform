package com.peng.sms.task;


import com.peng.sms.client.CacheClient;
import com.peng.sms.util.MailUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class MonitorClientBalanceTask {

    // Customer balance limit: if below 500 units, send notification
    private final long balanceLimit = 500000; // Amount is in cents (or smallest currency unit)

    // Pattern to match client balance keys in cache
    private final String CLIENT_BALANCE_PATTERN = "client_balance:*";

    // Hash keys
    private final String BALANCE = "balance";
    private final String EMAIL = "extend1";

    // Email template
    private String text = "Dear customer, your balance on 【FengHuo SMS Platform】 is only %s units. Please recharge promptly to avoid interruption of SMS services!";

    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private MailUtil mailUtil;

    // XXL-JOB scheduled task
    @XxlJob("monitorClientBalanceTask")
    public void monitor() throws MessagingException {
        // 1. Query all customer balances
        Set<String> keys = cacheClient.keys(CLIENT_BALANCE_PATTERN);

        for (String key : keys) {
            // 2. Get balance and email from cache
            Map<Object, Object> map = cacheClient.hGetAll(key);
            Long balance = Long.parseLong(map.get(BALANCE).toString());
            String email = (String) map.get(EMAIL);

            // 3. If balance is below limit, send email
            if (balance < balanceLimit) {
                mailUtil.sendEmail(
                        email,
                        "【FengHuo SMS Platform】Balance Alert",
                        String.format(text, balance / 1000)
                );
            }
        }
    }
}