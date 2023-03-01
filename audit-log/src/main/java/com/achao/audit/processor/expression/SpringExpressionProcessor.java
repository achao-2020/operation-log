package com.achao.audit.processor.expression;

import com.achao.audit.entity.AuditSaveEntity;
import com.achao.audit.metedata.AuditLogMetadata;
import com.achao.audit.metedata.expression.IExpressionMetadata;
import com.achao.audit.service.IAuditSaveService;
import com.achao.audit.util.SpringElHelper;

import java.util.UUID;

/**
 * @author achao
 */
public class SpringExpressionProcessor extends ExpressionAbstractProcessor{

    private SpringElHelper springElHelper;

    public SpringExpressionProcessor(SpringElHelper springElHelper, IAuditSaveService auditSaveService) {
        super(auditSaveService);
        this.springElHelper = springElHelper;
    }


    @Override
    protected AuditSaveEntity handleSaveEntity(AuditLogMetadata auditLogMetadata) {
        IExpressionMetadata spElMetadata = auditLogMetadata.getSpElMetadata();
        String springEl = spElMetadata.getExpression();
        Object[] args = spElMetadata.getArgs();
        String detail = springElHelper.spElRender(springEl, args);
        AuditSaveEntity saveEntity = new AuditSaveEntity();
        saveEntity.setDetail(detail);
        saveEntity.setLogId(UUID.randomUUID().toString());
        saveEntity.setLogLevel(auditLogMetadata.getLogLevel().toString());
        saveEntity.setActionName(auditLogMetadata.getOperationName());
        saveEntity.setActionType(String.valueOf(auditLogMetadata.getActionType()));
        saveEntity.setRequestUrl(auditLogMetadata.getRequestUrl());
        saveEntity.setResourceType(auditLogMetadata.getResourceType());
        saveEntity.setResourceId("");
        saveEntity.setResult(auditLogMetadata.getResult());
        saveEntity.setVisitorIp(auditLogMetadata.getVisitorIp());
        saveEntity.setRequestParam(auditLogMetadata.getRequestParam());
        saveEntity.setErrorMsg(auditLogMetadata.getErrorMsg());
        return saveEntity;
    }
}
