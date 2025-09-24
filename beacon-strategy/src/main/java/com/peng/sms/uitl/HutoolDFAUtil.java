package com.peng.sms.uitl;

import cn.hutool.dfa.WordTree;
import com.peng.sms.client.BeaconCacheClient;
import com.peng.sms.constant.CacheConstant;

import java.util.List;
import java.util.Set;

public class HutoolDFAUtil {

    private static WordTree wordTree = new WordTree();

    public static void init() {
        BeaconCacheClient cacheClient = (BeaconCacheClient) SpringUtil.getBeanByClass(BeaconCacheClient.class);
        Set<String> dirtyWords = cacheClient.smember(CacheConstant.DIRTY_WORD);
        wordTree.addWords(dirtyWords);
    }

    public static List<String> getDirtyWord(String text) {
        return wordTree.matchAll(text);
    }
}
