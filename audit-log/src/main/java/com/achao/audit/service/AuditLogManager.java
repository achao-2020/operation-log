package com.achao.audit.service;

import cn.hutool.core.lang.UUID;
import com.achao.audit.common.ActionType;
import com.achao.audit.metedata.AuditLogMetadata;
import com.achao.audit.processor.IAuditProcessor;
import com.achao.audit.processor.expression.ExpressionAbstractProcessor;
import com.achao.audit.util.AuditThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

/**
 * 审计日志处理上下文
 */
public class AuditLogManager {

    private Logger log = LoggerFactory.getLogger(AuditLogManager.class);

    private Map<String, IAuditProcessor> iAuditProcessorMap;

    public AuditLogManager(Map<String, IAuditProcessor> iAuditProcessorMap) {
        this.iAuditProcessorMap = iAuditProcessorMap;
    }

    public void resolveAuditLog() throws IllegalAccessException, NoSuchFieldException {
        AuditLogMetadata auditLogMetadata = AuditThreadLocal.get();
        if (auditLogMetadata.getSpElMetadata() != null) {
            // 表达式渲染审计日志
            iAuditProcessorMap.get("expression").resolveAuditLog(auditLogMetadata);
        } else {
            IAuditProcessor processor = getAuditAbstractService();
            processor.resolveAuditLog(auditLogMetadata);
        }
    }

    /**
     * 处理增加，修改审计日志方法入口
     */
    public void resolveEditAuditLog() throws IllegalAccessException, NoSuchFieldException {
        AuditLogMetadata auditLogMetadata = AuditThreadLocal.get();
        if (!auditLogMetadata.getActionType().equals(ActionType.DELETE)) {
            resolveAuditLog();
        }
    }

    /**
     * 处理删除审计日志方法入口
     */
    public void resolveDeleteAuditLog() throws IllegalAccessException, NoSuchFieldException {
        AuditLogMetadata auditLogMetadata = AuditThreadLocal.get();
        if (auditLogMetadata.getActionType().equals(ActionType.DELETE)) {
            auditLogMetadata.setResult(1);
            auditLogMetadata.setLogId(UUID.randomUUID().toString());
            resolveAuditLog();
        }
    }

    /**
     * 更新操作失败状态审计日志方法入口
     */
    public void resolveEditAuditLog(String errorMsg) throws IllegalAccessException, NoSuchFieldException {
        AuditLogMetadata auditLogMetadata = AuditThreadLocal.get();
        auditLogMetadata.setResult(0);
        auditLogMetadata.setErrorMsg(errorMsg);
        resolveEditAuditLog();
    }

    public IAuditProcessor getAuditAbstractService() {
        AuditLogMetadata auditLogMetadata = AuditThreadLocal.get();
        IAuditProcessor iAuditProcessor = iAuditProcessorMap.get(String.valueOf(auditLogMetadata.getActionType()));
        if (Objects.isNull(iAuditProcessor)) {
            log.error("没有定义处理审计的服务！");
        }
        return iAuditProcessor;
    }
}
