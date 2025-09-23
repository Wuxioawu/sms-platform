package com.peng.sms.api.filter.impl;

import com.peng.sms.api.client.BeaconCacheClient;
import com.peng.sms.api.filter.CheckFilter;
import com.peng.sms.constant.ApiConstant;
import com.peng.sms.constant.CacheConstant;
import com.peng.sms.enums.ExceptionEnums;
import com.peng.sms.exception.ApiException;
import com.peng.sms.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.util.Map;
import java.util.Set;

/**
 * check the requestion paramenter template
 * the message the contant
 */
@Service(value = "template")
@Slf4j
public class TemplateCheckFilter implements CheckFilter {

    @Autowired
    private BeaconCacheClient cacheClient;

    /**
     * the text of the template
     */
    private final String TEMPLATE_TEXT = "templateText";

    private final String TEMPLATE_PLACEHOLDER = "#";

    @Override
    public void check(StandardSubmit submit) {
        log.info("{interface module} ->  {check template filter} ->  is running");

        String text = submit.getText().replace(ApiConstant.SIGN_PREFIX + submit.getSign() + ApiConstant.SIGN_SUFFIX, "");

        Set<Map> templates = cacheClient.smember(CacheConstant.CLIENT_TEMPLATE + submit.getSignId());

        if (templates == null || templates.isEmpty()) {
            log.info("{interface module} ->  {check template filter} ->  templates is empty");
            throw new ApiException(ExceptionEnums.ERROR_TEMPLATE);
        }

        for (Map template : templates) {
            String templateText = (String) template.get(TEMPLATE_TEXT);

            if (text.equals(templateText)) {
                log.info("{interface module} ->  {check template filter} ->  check pass, total match  templateText = {}", templateText);
                return;
            }

            if (templateText != null && templateText.contains(TEMPLATE_PLACEHOLDER)
                    && templateText.length() - templateText.replaceAll(TEMPLATE_PLACEHOLDER, "").length() == 2) {
                String templateTextPrefix = templateText.substring(0, templateText.indexOf(TEMPLATE_PLACEHOLDER));
                String templateTextSuffix = templateText.substring(templateText.lastIndexOf(TEMPLATE_PLACEHOLDER) + 1);
                if (text.startsWith(templateTextPrefix) && text.endsWith(templateTextSuffix)) {
                    log.info("{interface module} ->  {check template filter} ->  pass, match the placeholder ->  templateText = {}", templateText);
                    return;
                }
            }
        }

        log.info("{interface module} ->  {check template filter} ->  there is no template = {}", text);
        throw new ApiException(ExceptionEnums.ERROR_TEMPLATE);
    }
}
