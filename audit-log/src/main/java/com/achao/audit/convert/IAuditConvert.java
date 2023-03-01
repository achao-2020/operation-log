package com.achao.audit.convert;

import java.util.Map;

/**
 * @author achao
 * @date 2022-11-22 16:23
 */
public interface IAuditConvert<T> {
    /**
     * 前置处理审计日志包装类
     * @param map key 参数名，value 参数值
     * @return
     */
    default T preWrapper(Map<String, Object> map){
        return null;
    }

    /**
     * 后置处理审计日志包装类
     * @return
     */
    default T postWrapper() {
        return null;
    }
}