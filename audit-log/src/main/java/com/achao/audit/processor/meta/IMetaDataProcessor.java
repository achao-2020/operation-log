package com.achao.audit.processor.meta;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 审计元数据处理类
 * @author achao
 * @date 2022/12/12 23:51
 */
public interface IMetaDataProcessor {
    /**
     * 初始化审计元数据
     * @param joinPoint
     */
    void initMeteData(ProceedingJoinPoint joinPoint);
}
