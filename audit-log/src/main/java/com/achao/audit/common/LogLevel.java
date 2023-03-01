package com.achao.audit.common;

/**
 * @author achao
 */
public enum LogLevel {
    /**
     * 危险程度——轻危
     */
    LOW("轻危"),
    /**
     * 危险程度——一般
     */
    NORMAL("一般"),
    /**
     * 危险程度——高危
     */
    HIGHT("高危");

    String name;

    LogLevel(String name) {
        this.name = name;
    }
}
