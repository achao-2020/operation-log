package com.achao.audit.desensitize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author achao
 */
public class EmailPattern extends BasePattern {

    @Override
    public String desData(String email) {
        if (StringUtils.isBlank(email)) {
            return email;
        }
        int index = StringUtils.indexOf(email, "@");
        if (index <= 1 || email.length() < 2) {
            return email;
        } else {
            return StringUtils.join(StringUtils.left(email, 2), "*****", email.substring(index));
        }
    }
}
