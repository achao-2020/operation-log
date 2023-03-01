package com.achao.audit.service;

import com.achao.audit.entity.AuditSaveEntity;
import com.achao.audit.metedata.AuditLogMetadata;

/**
 * 审计日志保存接口
 */
public interface IAuditSaveService {
    /**
     * 持久化保存审计日志方法
     * @param saveDto
     * @param auditLogMetadata
     * @return
     */
    AuditSaveEntity saveAuditLog(AuditSaveEntity saveDto, AuditLogMetadata auditLogMetadata);

    /**
     * 获取最近一次审计日志信息
     * @param tenantId
     * @param resourceId
     * @return
     */
    AuditSaveEntity recentlyUpdateLog(String tenantId, String resourceId);
}
