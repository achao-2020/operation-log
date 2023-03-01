package com.achao.audit.desensitize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author achao
 */
public class AddressPattern extends BasePattern {
    @Override
    public String desData(String address) {
        if (isContainChinese(address)) {
            if (address.contains("区")) {
                int index = StringUtils.indexOf(address, "区");
                return StringUtils.join(StringUtils.left(address, index + 1), "*****");
            } else if (address.contains("市")) {
                int index = StringUtils.indexOf(address, "市");
                return StringUtils.join(StringUtils.left(address, index + 1), "*****");
            } else {
                return StringUtils.join(StringUtils.left(address, address.length() >> 1), "*****");
            }
        } else {
            if (address.contains(" ")) {
                String[] split = address.split(" ");
                return StringUtils.join("*** *** ", split[split.length - 2], " ", split[split.length - 1]);
            } else if (address.contains(",")) {
                String[] split = address.split(",");
                return StringUtils.join("*** *** ", split[split.length - 2], ",", split[split.length - 1]);
            } else {
                return StringUtils.join("*** *** ", StringUtils.right(address, address.length() >> 1));
            }
        }
    }
}
