package com.achao.audit.processor;

import com.achao.audit.entity.AuditSaveEntity;
import com.achao.audit.metedata.AuditLogMetadata;

/**
 * 审计日志处理器
 *
 * @author achao
 */
public interface IAuditProcessor {
    AuditSaveEntity resolveAuditLog(AuditLogMetadata auditLogMetadata) throws IllegalAccessException, NoSuchFieldException;
}
