package com.peng.sms.task;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
 import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestTask {

    @XxlJob("test")
    public void taskExecutor() {
        System.out.println("hello word");
    }
}
