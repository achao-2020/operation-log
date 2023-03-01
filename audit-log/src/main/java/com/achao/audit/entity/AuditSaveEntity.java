package com.achao.audit.entity;

import java.util.Map;

/**
 * @author achao
 */
public class AuditSaveEntity {
    /**
     * 唯一id
     */
    private String logId;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 日志级别
     */
    private String logLevel;
    /**
     * 操作名称
     */
    private String actionName;
    /**
     * 操作详情
     */
    private String detail;
    /**
     * 请求地址
     */
    private String requestUrl;
    /**
     * 资源类型
     */
    private String resourceType;
    /**
     * 资源对象名称
     */
    private String resourceName;
    /**
     * 资源对象id
     */
    private String resourceId;
    /**
     * 请求结果
     */
    private Integer result;
    /**
     * 失败信息
     */
    private String errorMsg;
    /**
     * 操作类型，ADD,UPDATE,DELETE,SEARCH
     */
    private String actionType;
    /**
     * 访问者ip
     */
    private String visitorIp;
    /**
     * 请求入参
     */
    private String requestParam;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getVisitorIp() {
        return visitorIp;
    }

    public void setVisitorIp(String visitorIp) {
        this.visitorIp = visitorIp;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }
}
