package com.achao.audit.aspect;

import com.achao.audit.processor.meta.IMetaDataProcessor;
import com.achao.audit.service.AuditLogManager;
import com.achao.audit.util.AuditThreadLocal;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author achao
 * @date 2022-11-21 17:02
 */
@Aspect
public class AuditAspect {

    private static Logger log = Logger.getLogger(AuditAspect.class);

    private IMetaDataProcessor defaultMetaDataProcessor;

    private AuditLogManager auditLogManager;

    @Autowired
    public AuditAspect(IMetaDataProcessor defaultMetaDataProcessor, AuditLogManager auditLogManager) {
        this.defaultMetaDataProcessor = defaultMetaDataProcessor;
        this.auditLogManager = auditLogManager;
    }

    @Pointcut("@annotation(com.achao.audit.annotation.AuditLog)")
    public void auditPoint() {

    }

    @Around("auditPoint()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable{

        log.debug("进入审计日志doAround方法");
        try {
            // 初始化审计元数据
            defaultMetaDataProcessor.initMeteData(joinPoint);
            // 处理审计日志（删除）
            auditLogManager.resolveDeleteAuditLog();
            Object result = joinPoint.proceed();
            return result;

        } catch (Exception e) {
            throw e;
        } finally {
            // 释放线程变量
            AuditThreadLocal.remove();
        }
    }


    @AfterReturning("auditPoint()")
    public void doAfterReturning(JoinPoint joinPoint) {
        log.debug("进入审计日志doAfterReturning方法");
        try{
            // 记录其他类型的审计日志（新增，修改）
            auditLogManager.resolveEditAuditLog();
        } catch (Exception e) {
            log.warn("审计日志afterReturning出错！");
        } finally {
            // 释放线程变量
            AuditThreadLocal.remove();
        }
    }

    @AfterThrowing(pointcut = "auditPoint()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) throws Throwable {
        log.debug("进入审计日志doAfterThrowing方法");
        try {
            auditLogManager.resolveEditAuditLog(e.getMessage());
        } catch (Exception exception) {
            log.warn("审计日志doAfterThrowing出错！");
        } finally {
            // 释放线程变量
            AuditThreadLocal.remove();
        }
        throw e;
    }
}