package com.peng.sms.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peng.sms.client.CacheClient;
import com.peng.sms.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestData {

    @Autowired
    private ClientBalanceMapper clientBalanceMapper;

    @Autowired
    private ClientBusinessMapper clientBusinessMapper;

    @Autowired
    private ClientSignMapper clientSignMapper;

    @Autowired
    private ClientTemplateMapper clientTemplateMapper;

    @Autowired
    private MobileAreaMapper mobileAreaMapper;

    @Autowired
    private MobileDirtyWordMapper mobileDirtyWordMapper;

    @Autowired
    private MobileBlackMapper mobileBlackMapper;

    @Autowired
    private MobileTransferMapper mobileTransferMapper;

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private ClientChannelMapper clientChannelMapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    public void insertDataToRedis() throws JsonProcessingException {
        ClientBalanceMapperTestFindByClientId();
        ClientBusinessMapperFindById();
        ClientSignMapperTestFindByClientId();
        ClientTemplateMapperTestFindBySignId();
        MobileAreaMapperFindAll();
        MobileDirtyWordMapperFindDirtyWord();
        MobileBlackMapperTestFindAll();
        MobileTransferMapperTestFindAll();
        ChannelMapperTestFindAll();
        ClientChannelMapperTestFindAll();
    }

    void ClientChannelMapperTestFindAll() {
        List<ClientChannel> list = clientChannelMapper.findAll();
        for (ClientChannel clientChannel : list) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = null;
            try {
                map = objectMapper.readValue(objectMapper.writeValueAsString(clientChannel), Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            cacheClient.sadd("client_channel:" + clientChannel.getClientId(), map);
        }
    }

    void ChannelMapperTestFindAll() {
        List<Channel> list = channelMapper.findAll();
        for (Channel channel : list) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = null;
            try {
                map = objectMapper.readValue(objectMapper.writeValueAsString(channel), Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            cacheClient.hmset("channel:" + channel.getId(), map);
        }
    }

    void MobileTransferMapperTestFindAll() {
        List<MobileTransfer> list = mobileTransferMapper.findAll();
        for (MobileTransfer mobileTransfer : list) {
            cacheClient.set("transfer:" + mobileTransfer.getTransferNumber(), mobileTransfer.getNowIsp());
        }
    }

    void MobileBlackMapperTestFindAll() {
        List<MobileBlack> mobileBlackList = mobileBlackMapper.findAll();
        for (MobileBlack mobileBlack : mobileBlackList) {
            if (mobileBlack.getClientId() == 0) {
                // platform
                cacheClient.set("black:" + mobileBlack.getBlackNumber(), "1");
            } else {
                // client
                cacheClient.set("black:" + mobileBlack.getClientId() + ":" + mobileBlack.getBlackNumber(), "1");
            }
        }
    }

    void MobileDirtyWordMapperFindDirtyWord() {
        List<String> dirtyWords = mobileDirtyWordMapper.findDirtyWord();
        cacheClient.saddStr("dirty_word", dirtyWords.toArray(new String[]{}));
        System.out.println("MobileDirtyWordMapperTest : finish the test");
    }

    void MobileAreaMapperFindAll() {
        List<MobileArea> list = mobileAreaMapper.findAll();
        Map<String, String> map = new HashMap(list.size());
        for (MobileArea mobileArea : list) {
            map.put("phase:" + mobileArea.getMobileNumber(), mobileArea.getMobileArea() + "," + mobileArea.getMobileType());
        }
        cacheClient.pipelineString(map);
    }

    void ClientTemplateMapperTestFindBySignId() {
        List<ClientTemplate> ct_15 = clientTemplateMapper.findBySignId(15L);
        List<ClientTemplate> ct_24 = clientTemplateMapper.findBySignId(24L);

        for (ClientTemplate ct : ct_15) {
            System.out.println(ct);
        }

        // no have data from in database
        for (ClientTemplate ct : ct_24) {
            System.out.println(ct);
        }

        ObjectMapper mapper = new ObjectMapper();
        List<Map> collect = ct_15.stream().map(ct -> {
            try {
                return mapper.readValue(mapper.writeValueAsString(ct), Map.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());

        cacheClient.sadd("client_template:15", collect.toArray(new Map[]{}));
    }

    void ClientSignMapperTestFindByClientId() {
        List<ClientSign> list = clientSignMapper.findByClientId(1L);
        for (ClientSign clientSign : list) {
            System.out.println(clientSign);
        }
        ObjectMapper objectMapper = new ObjectMapper();

        List<Map> collect = list.stream().map(cs -> {
            try {
                return objectMapper.readValue(objectMapper.writeValueAsString(cs), Map.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());

        cacheClient.sadd("client_sign:1", collect.toArray(new Map[]{}));
    }

    void ClientBusinessMapperFindById() throws JsonProcessingException {
        ClientBusiness byId = clientBusinessMapper.findById(1L);
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(objectMapper.writeValueAsString(byId), Map.class);
        cacheClient.hmset("client_business:" + byId.getApikey(), map);
    }

    void ClientBalanceMapperTestFindByClientId() throws JsonProcessingException {
        ClientBalance clientBalance = clientBalanceMapper.findByClientId(1L);
        System.out.println(clientBalance);
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(objectMapper.writeValueAsString(clientBalance), Map.class);
        cacheClient.hmset("client_balance:1", map);
    }


}
