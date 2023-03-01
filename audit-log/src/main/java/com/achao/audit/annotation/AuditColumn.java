package com.achao.audit.annotation;

import com.achao.audit.common.ColumnType;
import com.achao.audit.common.DesType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 审计字段注解
 * @author achao
 * @date 2022-11-21 17:05
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditColumn {
    /**
     * 字段名称
     */
    String name() default "";

    /**
     * 是否进行审计（若是id，可以选择false)
     */
    boolean isAudit() default true;

    /**
     * 字段类型
     */
    ColumnType columnType() default ColumnType.NORMAL;
    /**
     * 脱敏类型
     */
    DesType dstBean() default DesType.DST_NULL;
}
