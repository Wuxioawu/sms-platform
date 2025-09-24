package com.peng.sms.uitl;

import com.peng.sms.client.BeaconCacheClient;
import com.peng.sms.constant.CacheConstant;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class DFAUtil {
    // Build the sensitive word tree.
    private static Map dfaMap = new HashMap<>();

    private static final String IS_END = "isEnd";

    private static final String NOT_END = "0";
    private static final String ALREADY_END = "1";

    public static void init() {
        // Get cacheClient from the Spring container
        BeaconCacheClient cacheClient = (BeaconCacheClient) SpringUtil.getBeanByClass(BeaconCacheClient.class);
        // Get all sensitive words stored in Redis
        Set<String> dirtyWords = cacheClient.smember(CacheConstant.DIRTY_WORD);
        // Call create() to build the sensitive word tree into dfaMap
        create(dirtyWords);
    }

    public static void create(Set<String> dirtyWords) {
        // 1. Declare a Map as temporary storage
        Map nowMap;

        // 2. Iterate through the sensitive word set
        for (String dirtyWord : dirtyWords) {
            nowMap = dfaMap;
            // For each word, process character by character
            for (int i = 0; i < dirtyWord.length(); i++) {
                // Get each character of the sensitive word
                String word = String.valueOf(dirtyWord.charAt(i));
                // Check if the current character already exists in the tree
                Map map = (Map) nowMap.get(word);
                if (map == null) {
                    // If the character does not exist, create a new branch
                    map = new HashMap();
                    nowMap.put(word, map);
                }
                // Move deeper into the tree
                nowMap = map;
                // If IS_END exists and equals 1, skip updating
                if (nowMap.containsKey(IS_END) && nowMap.get(IS_END).equals(ALREADY_END)) {
                    continue;
                }
                // If at the last character, mark the end; otherwise, mark as not ended
                if (i == dirtyWord.length() - 1) {
                    nowMap.put(IS_END, ALREADY_END);
                } else {
                    nowMap.putIfAbsent(IS_END, NOT_END);
                }
            }
        }
    }

    /**
     * Find all sensitive words from the given text using the sensitive word tree
     *
     * @param text Input text
     * @return Set of matched sensitive words
     */
    public static Set<String> getDirtyWord(String text) {
        // 1. Store matched sensitive words
        Set<String> dirtyWords = new HashSet<>();
        // 2. Iterate through the text
        for (int i = 0; i < text.length(); i++) {
            // Temporary variables for index tracking
            int nextLength = 0;
            int dirtyLength = 0;
            // Get the root map of the trie
            Map nowMap = dfaMap;
            // Outer loop moves index forward; inner loop continues matching characters
            for (int j = i; j < text.length(); j++) {
                // Get the current character
                String word = String.valueOf(text.charAt(j));
                // Match against the current map
                nowMap = (Map) nowMap.get(word);
                // If no matching branch exists, break
                if (nowMap == null) {
                    break;
                } else {
                    // Count the length of the matched word
                    dirtyLength++;
                    // If IS_END equals 1, full sensitive word found
                    if (ALREADY_END.equals(nowMap.get(IS_END))) {
                        nextLength = dirtyLength;
                        break;
                    }
                }
            }
            // If a sensitive word was matched
            if (nextLength > 0) {
                // Add it to the result set and move outer index forward
                dirtyWords.add(text.substring(i, i + nextLength));
                i = i + nextLength - 1; // -1 because the for loop will increment i
            }
        }
        // Return the set of matched sensitive words
        return dirtyWords;
    }
}
