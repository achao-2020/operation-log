package com.achao.org.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import com.achao.audit.entity.AuditSaveEntity;
import com.achao.audit.metedata.AuditLogMetadata;
import com.achao.audit.service.IAuditSaveService;
import com.achao.audit.util.AuditThreadLocal;
import com.achao.org.dao.AuditLog;
import com.achao.org.mapper.AuditLogMapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 98537
 * @description 针对表【audit_log】的数据库操作Service实现
 * @createDate 2022-12-22 23:34:57
 */
@Service
public class AuditLogService extends ServiceImpl<AuditLogMapper, AuditLog> implements IAuditSaveService {

    @Override
    public AuditSaveEntity saveAuditLog(AuditSaveEntity saveEntity, AuditLogMetadata auditLogMetadata) {
        saveEntity.setLogLevel(auditLogMetadata.getLogLevel().toString());
        saveEntity.setActionName(auditLogMetadata.getOperationName());
        saveEntity.setRequestUrl(auditLogMetadata.getRequestUrl());
        saveEntity.setResourceType(auditLogMetadata.getResourceType());
        saveEntity.setActionType(auditLogMetadata.getResourceType());
        saveEntity.setVisitorIp(auditLogMetadata.getVisitorIp());
        saveEntity.setRequestParam(JSONObject.toJSONString(auditLogMetadata.getArgs()));
        if (StringUtils.isEmpty(saveEntity.getResourceId())) {
            saveEntity.setResourceId("无资源id");
        }
        if (StringUtils.isEmpty(saveEntity.getResourceName())) {
            saveEntity.setResourceName("无资源名称");
        }
        saveEntity.setLogId(UUID.fastUUID().toString());
        saveEntity.setErrorMsg(auditLogMetadata.getErrorMsg());
        saveEntity.setResult(auditLogMetadata.getResult());
        AuditLog auditLog = new AuditLog();
        BeanUtils.copyProperties(saveEntity, auditLog);
        this.save(auditLog);
        return saveEntity;
    }

    @Override
    public AuditSaveEntity recentlyUpdateLog(String tenantId, String resourceId) {
        List<AuditLog> auditLogs = this.list(Wrappers.<AuditLog>lambdaQuery().eq(StringUtils.isNoneEmpty(tenantId), AuditLog::getTenantId, tenantId)
                .eq(StringUtils.isNoneEmpty(resourceId), AuditLog::getResourceId, resourceId).orderByDesc(AuditLog::getId));
        AuditSaveEntity auditSaveEntity = new AuditSaveEntity();
        BeanUtils.copyProperties(auditLogs.get(0), auditSaveEntity);
        return auditSaveEntity;
    }

}




