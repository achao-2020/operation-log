package com.achao.audit.desensitize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author achao
 */
public abstract class BasePattern {
    /**
     * 判断是否包含中文
     */
    protected static final Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");
    /**
     * 判断是否纯数字
     */
    protected static final Pattern NUMERIC_PATTERN = Pattern.compile("[0-9]*");

    /**
     * 脱敏数据
     * @param value
     * @return
     */
    public abstract String desData(String value);

    /**
     * 判断是否为数字
     * @param str
     * @return
     */
    protected boolean isNumeric(String str){
        return NUMERIC_PATTERN.matcher(str).matches();
    }

    /**
     * 判断是否包含中文
     */
    protected boolean isContainChinese(String str) {
        Matcher m = CHINESE_PATTERN.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

}
