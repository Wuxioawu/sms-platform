package com.peng.sms.mapper;

import com.peng.sms.client.CacheClient;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class MobileDirtyWordMapperTest {

    private static final Logger log = LoggerFactory.getLogger(MobileDirtyWordMapperTest.class);

    @Autowired
    private MobileDirtyWordMapper mapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    void findDirtyWord() {
        List<String> dirtyWords = mapper.findDirtyWord();
        cacheClient.saddStr("dirty_word", dirtyWords.toArray(new String[]{}));
        log.info("MobileDirtyWordMapperTest : finish the test" );
    }
}