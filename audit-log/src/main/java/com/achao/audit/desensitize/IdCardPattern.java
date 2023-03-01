package com.achao.audit.desensitize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author achao
 */
public class IdCardPattern extends BasePattern{
    @Override
    public String desData(String idCard) {
        if (idCard.length() <= 4) {
            return StringUtils.join(idCard, "*******");
        }
        return StringUtils.join(idCard.substring(0, 4), "*******");
    }
}
