package com.achao.audit.util;

import com.achao.audit.metedata.AuditLogMetadata;

/**
 * @author achao
 */

public class AuditThreadLocal {

    private static final ThreadLocal<AuditLogMetadata> threadLocal = new ThreadLocal<AuditLogMetadata>();

    public AuditThreadLocal() {
    }

    public static void set(AuditLogMetadata auditLogMetadata) {
        threadLocal.set(auditLogMetadata);
    }

    public static AuditLogMetadata get() {
        return threadLocal.get() == null ? new AuditLogMetadata() : threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

}
