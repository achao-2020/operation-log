package com.achao.audit.processor.meta;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.bean.BeanUtil;
import com.achao.audit.annotation.AuditLog;
import com.achao.audit.metedata.AuditLogMetadata;
import com.achao.audit.metedata.convert.ConvertMetaData;
import com.achao.audit.metedata.expression.SpElMetadata;
import com.achao.audit.util.AuditThreadLocal;
import com.achao.audit.util.RequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

/**
 * 审计初始化元数据默认实现
 * @author achao
 */
public class DefaultMetaDataProcessor implements IMetaDataProcessor{
    private Logger log = LoggerFactory.getLogger(DefaultMetaDataProcessor.class);

    /**
     * 初始化元数据信息
     * @param joinPoint
     */
    @Override
    public void initMeteData(ProceedingJoinPoint joinPoint) {
        // 元数据信息放入线程变量中
        AuditLogMetadata auditLogMetadata = AuditThreadLocal.get();

        // 获取 RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 获取 HttpServletRequest
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        // 访问者ip地址
        String visitorIp = RequestUtil.getIpAddr(request);
        Object[] args = joinPoint.getArgs();
        Object[] newArgs = new Object[args.length];
        // 深度拷贝入参
        for (int i = 0; i < args.length; i++) {
            newArgs[i] = deepClone(args[i]);
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method apiMethod = signature.getMethod();
        String requestUrl = RequestUtil.getRestUrl(apiMethod);
        AuditLog auditLog = AnnotationUtil.getAnnotation(apiMethod, AuditLog.class);
        auditLogMetadata.setActionType(auditLog.actionType());
        auditLogMetadata.setLogLevel(auditLog.logLevel());
        auditLogMetadata.setResourceType(auditLog.resourceType());
        auditLogMetadata.setVisitorIp(visitorIp);
        auditLogMetadata.setRequestUrl(requestUrl);
        String actionName = auditLog.actionName();
        auditLogMetadata.setOperationName(actionName);
        auditLogMetadata.setApiMethod(apiMethod);
        auditLogMetadata.setResult(1);
        if (StringUtils.isNotEmpty(auditLog.springEl())) {
            // 简单的摘要输出
            SpElMetadata spElMetadata = new SpElMetadata();
            spElMetadata.setSpringEl(auditLog.springEl());
            spElMetadata.setArgs(newArgs);
            auditLogMetadata.setSpElMetadata(spElMetadata);
        } else if (Objects.nonNull(auditLog.convertClass())) {
            // 包装类基本元素
            ConvertMetaData wrapperMetadata = new ConvertMetaData();
            wrapperMetadata.setLastParamJson("");
            wrapperMetadata.setRequestParam(newArgs);
            wrapperMetadata.setConvertClass(auditLog.convertClass());
            auditLogMetadata.setAuditLogMetadata(wrapperMetadata);

        }
        auditLogMetadata.setRequestParam(RequestUtil.parseJsonBody(newArgs));
        AuditThreadLocal.set(auditLogMetadata);
    }

    /**
     * 对入参对象的深度拷贝
     * @param obj
     * @return
     */
    private Object deepClone(Object obj) {
        boolean primitive = false;
        if (obj == null || obj instanceof HttpServletRequest || obj instanceof HttpServletResponse) {
            return null;
        }
        try {
            // 判断是否为原始类型
            primitive = ((Class<?>) obj.getClass().getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            primitive = false;
        }
        if (primitive) {
            // 基本类型不拷贝
            return obj;
        }
        if (obj instanceof String) {
            // 字符串不拷贝
            return obj;
        }
        if (obj instanceof List) {
            // 如果是list，使用序列化深度拷贝
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(obj);
                oos.flush();
                ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray ());
                ObjectInputStream ois = new ObjectInputStream(bis);
                return ois.readObject();
            } catch (Exception e) {
                log.error("深度拷贝失败！");
            }

        }
        return BeanUtil.toBean(obj, obj.getClass());
    }
}
