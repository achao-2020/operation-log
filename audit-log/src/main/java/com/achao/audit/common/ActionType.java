package com.achao.audit.common;

/**
 * 支持审计的操作类型
 * @author achao
 * @date 2022-11-22 16:26
 */
public enum ActionType {
    ADD("增加"),
    UPDATE("更新"),
    DELETE("删除"),
    ADD_BATCH("批量保存");

    String name;

    ActionType(String name) {
        this.name = name;
    }
}
