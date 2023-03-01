package com.achao.audit.desensitize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author achao
 */
public class PhonePattern extends BasePattern {
    @Override
    public String desData(String phone) {
        //截取手机号后四位
        int length = phone.length();
        if (length <= 4) {
            return phone;
        }
        String last = phone.substring(length - 4);
        if (phone.startsWith("1") && length == 11) {
            return StringUtils.join(phone.substring(0, 3), "****", last);
        } else {
            return StringUtils.join(phone.substring(0, 2), "****", last);
        }
    }
}
