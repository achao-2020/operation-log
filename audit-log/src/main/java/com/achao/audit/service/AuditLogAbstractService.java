package com.achao.audit.service;

import com.achao.audit.metedata.AuditLogMetadata;

/**
 * @author achao
 */
public abstract class AuditLogAbstractService {

    public abstract void resolveAuditLog(AuditLogMetadata auditLogMetadata);
}
