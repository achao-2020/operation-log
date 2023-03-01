package com.achao.audit.config;

import com.achao.audit.aspect.AuditAspect;
import com.achao.audit.desensitize.BasePattern;
import com.achao.audit.processor.IAuditProcessor;
import com.achao.audit.processor.converter.DeleteConvertProcessor;
import com.achao.audit.processor.meta.DefaultMetaDataProcessor;
import com.achao.audit.processor.meta.IMetaDataProcessor;
import com.achao.audit.service.AuditLogManager;
import com.achao.audit.service.IAuditSaveService;
import com.achao.audit.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author achao
 * @date 2022/12/15 23:46
 */
@Configuration(proxyBeanMethods = false)
public class AuditLogAutoConfiguration {

    @Autowired
    private Map<String, BasePattern> desMap;

    @Autowired
    private Map<String, IAuditProcessor> iAuditProcessorMap;

    @Bean
    public AuditAspect auditAspect(IMetaDataProcessor defaultMetaDataProcessor, AuditLogManager auditLogManager) {
        return new AuditAspect(defaultMetaDataProcessor, auditLogManager);
    }

    @Bean
    public IMetaDataProcessor iMetaDataProcessor() {
        return new DefaultMetaDataProcessor();
    }

    @Bean
    @ConditionalOnMissingBean(IAuditProcessor.class)
    public IAuditProcessor iAuditProcessor(IAuditSaveService auditSaveService) {
        return new DeleteConvertProcessor(auditSaveService, desMap);
    }

    @Bean
    public AuditLogManager auditLogManager() {
        return new AuditLogManager(iAuditProcessorMap);
    }

    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }
}
