package com.achao.audit.common;

/**
 * @author achao
 * @date 2022-5-20 17:23
 */
public enum ColumnType {
    /**
     * 代表审计字段的唯一标识，用于ActionType为Update的审计内容渲染输出
     */
    ID(),
    /**
     * 代表审计对象名称
     */
    NAME(),
    /**
     * 正常审计字段标记
     */
    NORMAL();
}
