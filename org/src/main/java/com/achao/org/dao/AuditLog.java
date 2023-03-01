package com.achao.org.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName audit_log
 */
@TableName(value ="audit_log")
@Data
public class AuditLog implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
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
     * 审计行为名称
     */
    private String actionName;

    /**
     * 审计日志详情
     */
    private String detail;

    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 资源类型
     */
    private String resourceType;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 审计对象资源id
     */
    private String resourceId;

    /**
     * 访问结果，0失败，1成功
     */
    private Integer result;

    /**
     * 失败信息
     */
    private String errorMsg;

    /**
     * 审计类型
     */
    private String actionType;

    /**
     * 访问者ip
     */
    private String visitorIp;

    /**
     * 
     */
    private String requestParam;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}