package com.achao.audit.desensitize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author achao
 */
public class BirthdayPattern extends BasePattern{
    @Override
    public String desData(String birthday) {
        if (birthday.length() <= 4){
            return birthday;
        }
        if (birthday.contains(" ")) {
            String[] strings = birthday.split(" ");
            for (String string : strings) {
                if (isNumeric(string) && string.length() == 4){
                    return StringUtils.join(string, "****");
                }
            }
            return StringUtils.join(birthday.substring(0, 4), "****");
        }else {
            return StringUtils.join(birthday.substring(0, 4), "****");
        }
    }

}
