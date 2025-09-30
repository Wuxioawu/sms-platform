package com.peng.sms.service.impl;

import com.peng.sms.mapper.SmsMenuMapper;
import com.peng.sms.service.SmsMenuService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

@Service
public class SmsMenuServiceImpl implements SmsMenuService {

    @Resource
    private SmsMenuMapper menuMapper;

    @Override
    public List<Map<String, Object>> findUserMenu(Integer id) {

        List<Map<String, Object>> list = menuMapper.findMenuByUserId(id);

        List<Map<String, Object>> data = new ArrayList<>();

        ListIterator<Map<String, Object>> parentIterator = list.listIterator();
        while (parentIterator.hasNext()) {
            Map<String, Object> menu = parentIterator.next();
            if ((int) menu.get("type") == 0) {
                data.add(menu);
                parentIterator.remove();
            } else {
                break;
            }
        }

        for (Map<String, Object> parentMenu : data) {

            List<Map<String, Object>> sonMenuList = new ArrayList<>();
            ListIterator<Map<String, Object>> sonIterator = list.listIterator();
            while (sonIterator.hasNext()) {
                Map<String, Object> sonMenu = sonIterator.next();
                String parentMenuId = parentMenu.get("id").toString();
                String sonMenuID = sonMenu.get("parentId").toString();
                if (parentMenuId.equals(sonMenuID)) {
                    sonMenuList.add(sonMenu);
                    sonIterator.remove();
                }
            }
            parentMenu.put("list", sonMenuList);
        }

        return data;
    }
}
