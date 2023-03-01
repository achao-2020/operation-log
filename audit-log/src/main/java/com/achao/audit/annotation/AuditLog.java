package com.achao.audit.annotation;

import com.achao.audit.common.ActionType;
import com.achao.audit.common.LogLevel;
import com.achao.audit.convert.IAuditConvert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 审计日志基本注解
 * @author achao
 * @date 2022-11-21 17:03
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {
    /**
     * 操作类型, 不能为空，必须标识审计类型
     */
    ActionType actionType();

    /**
     * 日志级别
     */
    LogLevel logLevel() default LogLevel.NORMAL;

    /**
     * 操作名称，当前接口可以描述的操作名称
     */
    String actionName() default "unknown";

    /**
     * 资源类型，可以关联使用方的权限模型唯一值
     */
    String resourceType() default "unknown";

    /**
     * SpEl表达式，快速输出审计内容。
     */
    String springEl() default "";

    /**
     * 审计日志变更类，如果springEl不为空，则优先以springEl模板的方式输出渲染内容
     */
    Class convertClass() default IAuditConvert.class;
}
