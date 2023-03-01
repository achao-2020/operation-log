package com.achao.audit.config;

import com.achao.audit.desensitize.BasePattern;
import com.achao.audit.processor.converter.AddConvertProcessor;
import com.achao.audit.processor.converter.DeleteConvertProcessor;
import com.achao.audit.processor.converter.UpdateConvertProcessor;
import com.achao.audit.processor.expression.ExpressionAbstractProcessor;
import com.achao.audit.processor.expression.SpringExpressionProcessor;
import com.achao.audit.service.IAuditSaveService;
import com.achao.audit.util.SpringElHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author achao
 * @date 2022/12/16 00:02
 */
@Configuration(proxyBeanMethods = false)
public class AuditProcessorConfiguration {

    @Resource
    private IAuditSaveService auditSaveService;

    @Autowired
    private Map<String, BasePattern> desMap;

    @Bean(name = "expression")
    @ConditionalOnMissingBean(ExpressionAbstractProcessor.class)
    public ExpressionAbstractProcessor springExpressionProcessor(SpringElHelper springElHelper) {
        return new SpringExpressionProcessor(springElHelper, auditSaveService);
    }

    @Bean
    @ConditionalOnClass({SpringExpressionProcessor.class, IAuditSaveService.class})
    public SpringElHelper springElHelper() {
        return new SpringElHelper();
    }

    @Bean(name = "ADD")
    @ConditionalOnClass(IAuditSaveService.class)
    public AddConvertProcessor addConvertProcessor() {
        return new AddConvertProcessor(auditSaveService, desMap);
    }

    @Bean(name = "DELETE")
    @ConditionalOnClass(IAuditSaveService.class)
    public DeleteConvertProcessor deleteConvertProcessor() {
        return new DeleteConvertProcessor(auditSaveService, desMap);
    }

    @Bean(name = "UPDATE")
    @ConditionalOnClass(IAuditSaveService.class)
    public UpdateConvertProcessor updateConvertProcessor() {
        return new UpdateConvertProcessor(auditSaveService, desMap);
    }
}
