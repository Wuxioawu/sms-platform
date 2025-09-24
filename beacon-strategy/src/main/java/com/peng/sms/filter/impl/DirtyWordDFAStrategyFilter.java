package com.peng.sms.filter.impl;

import com.peng.sms.filter.StrategyFilter;
import com.peng.sms.model.StandardSubmit;
import com.peng.sms.uitl.DFAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service(value = "dfaDirtyWord")
@Slf4j
public class DirtyWordDFAStrategyFilter implements StrategyFilter {

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("[Strategy Module - DirtyWordDFAStrategyFilter] running ");

        String text = submit.getText();
        Set<String> dirtyWords = DFAUtil.getDirtyWord(text);
        if (!dirtyWords.isEmpty()) {
            log.info("[Strategy Module - DirtyWordDFAStrategyFilter]  the sms contains the diry words， dirtyWords = {}", dirtyWords);

        }
    }
}
