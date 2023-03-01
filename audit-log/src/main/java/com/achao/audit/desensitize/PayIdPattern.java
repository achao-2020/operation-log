package com.achao.audit.desensitize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author achao
 */
public class PayIdPattern extends BasePattern {
    @Override
    public String desData(String payId) {
        if (payId.length() <= 6) {
            return StringUtils.join(payId, "******");
        } else if (payId.length() < 11) {
            return StringUtils.join(payId.substring(0, 6), "******");
        }
        return StringUtils.join(payId.substring(0, 6), "******", payId.substring(payId.length() - 4));
    }
}
