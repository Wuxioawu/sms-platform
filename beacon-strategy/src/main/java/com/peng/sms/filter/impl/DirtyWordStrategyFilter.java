package com.peng.sms.filter.impl;

import com.peng.sms.client.BeaconCacheClient;
import com.peng.sms.constant.CacheConstant;
import com.peng.sms.filter.StrategyFilter;
import com.peng.sms.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service(value = "dirtyword")
@Slf4j
public class DirtyWordStrategyFilter implements StrategyFilter {

    @Autowired
    private BeaconCacheClient beaconCacheClient;

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("[Strategy Module - DirtyWordStrategyFilter] running ");
        IKSegmenter ik = new IKSegmenter(new StringReader(submit.getText()), true);
        Set<String> contents = new HashSet<>();
        Lexeme lex = null;

        while (true) {
            try {
                if ((lex = ik.next()) == null) break;
            } catch (IOException e) {
                log.info("[Strategy Module - DirtyWordStrategyFilter]  IK analysis find error when it deal with the text content  e = {}", e.getMessage());
            }
            contents.add(lex.getLexemeText());
        }

        //3、 get the interaction set
        Set<Object> dirtyWords = beaconCacheClient.sinterStr(UUID.randomUUID().toString(), CacheConstant.DIRTY_WORD, contents.toArray(new String[]{}));

        if (dirtyWords != null && !dirtyWords.isEmpty()) {
            log.info("【[Strategy Module - DirtyWordStrategyFilter]   the sms contains dirty words， dirtyWords = {}", dirtyWords);
        }
    }
}
