package com.achao.audit.desensitize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author achao
 */
public class PostCodePattern extends BasePattern {
    @Override
    public String desData(String postCode) {
        if (postCode.length() == 6 && isNumeric(postCode)) {
            //国内的邮政编码
            return StringUtils.join(StringUtils.left(postCode, 4), "*****");
        } else {
            //国外
            return StringUtils.join(StringUtils.left(postCode, 2), "*****");
        }
    }
}
