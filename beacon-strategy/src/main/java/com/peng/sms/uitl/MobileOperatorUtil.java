package com.peng.sms.uitl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
public class MobileOperatorUtil {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final String url = "https://cx.shouji.360.cn/phonearea.php?number=";

    private final String CODE = "code";
    private final String DATA = "data";
    private final String PROVINCE = "province";
    private final String CITY = "city";
    private final String SP = "sp";
    private final String SPACE = " ";
    private final String SEPARATE = ",";


    public String getMobileInfoBy360(String mobile) {
        String mobileInfoJSON = restTemplate.getForObject(url + mobile, String.class);
        Map map = null;

        try {
            map = objectMapper.readValue(mobileInfoJSON, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.info("[Strategy Module - MobileOperatorUtil] JsonProcessingException ");
        }

        Integer code = (Integer) map.get(CODE);

        if (code != 0) {
            log.info("[Strategy Module - MobileOperatorUtil] the request fail the code :{} ", code);
            return null;
        }

        Map<String, String> areaAndOperator = (Map<String, String>) map.get(DATA);

        return areaAndOperator.get(PROVINCE) + SPACE +
                areaAndOperator.get(CITY) + SEPARATE +
                areaAndOperator.get(SP);
    }
}
