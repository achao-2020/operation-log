package com.achao.audit.metedata.convert;

import com.achao.audit.convert.IAuditConvert;


/**
 * @author achao
 */
public class ConvertMetaData implements IConvertMetaData{
    private String lastParamJson;

    private Object[] requestParam;

    private Class<IAuditConvert> convertClass;

    public String getLastParamJson() {
        return lastParamJson;
    }

    public void setLastParamJson(String lastParamJson) {
        this.lastParamJson = lastParamJson;
    }

    public Object[] getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(Object[] requestParam) {
        this.requestParam = requestParam;
    }

    public Class<IAuditConvert> getConvertClass() {
        return convertClass;
    }

    public void setConvertClass(Class<IAuditConvert> convertClass) {
        this.convertClass = convertClass;
    }
}
