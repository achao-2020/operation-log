package com.achao.audit.processor.expression;

import com.achao.audit.entity.AuditSaveEntity;
import com.achao.audit.metedata.AuditLogMetadata;
import com.achao.audit.metedata.expression.IExpressionMetadata;
import com.achao.audit.processor.IAuditProcessor;
import com.achao.audit.service.IAuditSaveService;

/**
 * 表达式审计日志处理器抽象类
 *
 * @author achao
 */
public abstract class ExpressionAbstractProcessor implements IAuditProcessor {

    private IAuditSaveService auditSaveService;

    public ExpressionAbstractProcessor(IAuditSaveService auditSaveService) {
        this.auditSaveService = auditSaveService;
    }

    @Override
    public AuditSaveEntity resolveAuditLog(AuditLogMetadata auditLogMetadata) throws IllegalAccessException {
        AuditSaveEntity saveEntity = handleSaveEntity(auditLogMetadata);
        return auditSaveService.saveAuditLog(saveEntity, auditLogMetadata);
    }

    protected abstract AuditSaveEntity handleSaveEntity(AuditLogMetadata auditLogMetadata);

    ;
}
