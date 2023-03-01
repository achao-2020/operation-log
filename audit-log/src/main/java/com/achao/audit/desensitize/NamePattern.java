package com.achao.audit.desensitize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author achao
 */
public class NamePattern extends BasePattern {

    @Override
    public String desData(String name) {
        if (isContainChinese(name)) {
            if (name.length() > 2) {
                return StringUtils.join(name.substring(0, 1), "*", name.substring(name.length() - 1));
            } else {
                return StringUtils.join(name.substring(0, 1), "*");
            }
        } else {
            if (name.contains(" ")) {
                int index = StringUtils.indexOf(name, " ");
                return StringUtils.join(name.substring(0, index + 1), "****");
            } else {
                //没有名字空格,默认取值
                return StringUtils.join(name.substring(0, 1), "****");
            }
        }
    }
}
