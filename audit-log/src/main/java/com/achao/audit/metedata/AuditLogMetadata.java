package com.achao.audit.metedata;

import com.achao.audit.common.ActionType;
import com.achao.audit.common.LogLevel;
import com.achao.audit.metedata.convert.ConvertMetaData;
import com.achao.audit.metedata.expression.IExpressionMetadata;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Method;

/**
 * 审计日志元数据
 *
 * @author achao
 */
public class AuditLogMetadata {

    /**
     * 操作类型
     */
    private ActionType actionType;
    /**
     * 审计级别
     */
    private LogLevel logLevel;
    /**
     * 访问ip
     */
    private String visitorIp;
    /**
     * 资源id
     */
    private String resourceId;
    /**
     * 请求url
     */
    private String requestUrl;
    /**
     * 资源类型
     */
    private String resourceType;
    /**
     * 操作名称
     */
    private String operationName;
    /**
     * 接口方法
     */
    private Method apiMethod;
    /**
     * 表达式元数据
     */
    private IExpressionMetadata spElMetadata;
    /**
     * 审计转换类数据
     */
    private ConvertMetaData convertMetadata;
    /**
     * 接口访问状态，1成功，0失败
     */
    private Integer result;
    /**
     * 请求入参
     */
    private String requestParam;
    /**
     * 落库日志id
     */
    private String logId;
    /**
     * 失败错误信息
     */
    private String errorMsg;

    public Object[] getArgs() {
        if (this.convertMetadata == null) {
            return this.spElMetadata.getArgs();
        } else {
            return this.convertMetadata.getRequestParam();
        }
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public IExpressionMetadata getSpElMetadata() {
        return spElMetadata;
    }

    public void setSpElMetadata(IExpressionMetadata spElMetadata) {
        this.spElMetadata = spElMetadata;
    }

    public ConvertMetaData getConvertMetadata() {
        return convertMetadata;
    }

    public void setAuditLogMetadata(ConvertMetaData convertMetadata) {
        this.convertMetadata = convertMetadata;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getVisitorIp() {
        return visitorIp;
    }

    public void setVisitorIp(String visitorIp) {
        this.visitorIp = visitorIp;
    }

    public Method getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(Method apiMethod) {
        this.apiMethod = apiMethod;
    }

}