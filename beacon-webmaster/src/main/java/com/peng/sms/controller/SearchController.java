package com.peng.sms.controller;

import com.peng.sms.client.SearchClient;
import com.peng.sms.constant.WebMasterConstants;
import com.peng.sms.entity.ClientBusiness;
import com.peng.sms.entity.SmsUser;
import com.peng.sms.enums.ExceptionEnums;
import com.peng.sms.service.ClientBusinessService;
import com.peng.sms.service.SmsRoleService;
import com.peng.sms.util.R;
import com.peng.sms.vo.ResultVO;
import com.peng.sms.vo.SearchSmsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
public class SearchController {
    @Autowired
    private SmsRoleService roleService;

    @Resource
    private SearchClient searchClient;

    @Autowired
    private ClientBusinessService clientBusinessService;

    @GetMapping("/sys/search/list")
    public ResultVO list(@RequestParam Map map) {
        // 1. Determine if the currently logged-in user can query the requested client information
        // 1.1 Check if the user is logged in
        SmsUser smsUser = (SmsUser) SecurityUtils.getSubject().getPrincipal();
        if (smsUser == null) {
            log.info("[Get Client Information] User not logged in!!");
            return R.error(ExceptionEnums.NOT_LOGIN);
        }

        // 1.2 Extract clientID from request parameters
        String clientIDStr = (String) map.get("clientID");
        Long clientID = null;
        if (!StringUtils.isEmpty(clientIDStr)) {
            clientID = Long.parseLong(clientIDStr);
        }

        // 1.3 Get the user's role names to check if the user is an administrator
        Set<String> roleNames = roleService.getRoleName(smsUser.getId());
        if (roleNames != null && !roleNames.contains(WebMasterConstants.ROOT)) {
            // If not an admin, fetch all client IDs associated with this user
            List<ClientBusiness> clients = clientBusinessService.findByUserId(smsUser.getId());

            if (clientID == null) {
                // If no clientID is provided in the request, use all client IDs for this user
                List<Long> list = new ArrayList<>();
                for (ClientBusiness client : clients) {
                    list.add(client.getId());
                }
                map.put("clientID", list);
            } else {
                // If clientID is provided, verify that the user has access to this clientID
                boolean hasAccess = false;
                for (ClientBusiness client : clients) {
                    if (client.getId().equals(clientID)) {
                        hasAccess = true;
                        break;
                    }
                }
                if (!hasAccess) {
                    log.info("[Search SMS] User does not have sufficient permissions!!");
                    return R.error(ExceptionEnums.SMS_NO_AUTHOR);
                }
            }
        }

        // 2. Call the search module to query data, returning 'total' and 'rows'
        Map<String, Object> data = searchClient.findSmsByParameters(map);

        // 3. Check the total count; if zero, return normally
        Long total = Long.parseLong(data.get("total").toString());
        if (total == 0) {
            return R.ok(0L, null);
        }

        // 4. If data exists, map each result row to SearchSmsVO
        List<Map> list = (List<Map>) data.get("rows");
        List<SearchSmsVO> rows = new ArrayList<>();
        for (Map row : list) {
            SearchSmsVO vo = new SearchSmsVO();
            try {
                BeanUtils.copyProperties(vo, row);
            } catch (Exception e) {
                e.printStackTrace();
            }
            rows.add(vo);
        }

        // 5. Return response data
        return R.ok(total, rows);
    }

}
