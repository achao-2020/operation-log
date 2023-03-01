package com.achao.audit.processor.converter;

import com.achao.audit.convert.IAuditConvert;
import com.achao.audit.desensitize.BasePattern;
import com.achao.audit.entity.AuditSaveEntity;
import com.achao.audit.metedata.AuditLogMetadata;
import com.achao.audit.service.IAuditSaveService;
import org.slf4j.Logger;

import java.util.Map;

/**
 * 新增操作审计日志处理器
 */
public class AddConvertProcessor extends ConverterAbstractProcessor{

    public AddConvertProcessor(IAuditSaveService auditSaveService, Map<String, BasePattern> desMap) {
        super(auditSaveService, desMap);
    }

    @Override
    protected void renderDetail(AuditSaveEntity saveDto, IAuditConvert convert, AuditLogMetadata auditLogMetadata) throws IllegalAccessException {
        invokePostConvert(convert, auditLogMetadata);
        StringBuffer detail = convertDetails(convert);
        saveDto.setDetail(detail.substring(0, detail.length() - 1));
    }

    @Override
    protected void auditExtraAttribute(AuditSaveEntity saveDto, IAuditConvert wrapper, AuditLogMetadata auditLogMetadata) throws IllegalAccessException {
        injectResourceReflect(wrapper, saveDto);
    }
}
