package com.achao.audit.common;

/**
 * @author achao
 */
public enum DesType {
    /**
     * 不脱敏
     */
    DST_NULL(""),
    /**
     * 电话脱敏
     */
    DST_PHONE("#desPhone"),
    /**
     * 邮件脱敏
     */
    DST_EMAIL("#desEmail"),
    /**
     * 邮件脱敏
     */
    DST_ADDRESS("#desAddress"),

    /**
     * 生日脱敏
     */
    DES_BIRTHDAY("#desBirthday"),
    /**
     * 身份证脱敏
     */
    DES_ID_CARD("#desIdCard"),
    /**
     * 姓名脱敏
     */
    DES_NAME("#desName"),
    /**
     * 银行卡脱敏
     */
    DES_PLAY("#desPayId"),
    /**
     * 邮政编码
     */
    DES_POST_CODE("#desPostCode");

    String beanName;

    public String getBeanName() {
        return beanName;
    }

    DesType(String beanName) {
        this.beanName = beanName;
    }
}
