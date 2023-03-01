-- x_springboot.audit_log definition

CREATE TABLE `audit_log` (
                             `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                             `log_id` varchar(40) NOT NULL DEFAULT '',
                             `tenant_id` varchar(40) NOT NULL DEFAULT '' COMMENT '租户id',
                             `tenant_name` varchar(40) NOT NULL DEFAULT '' COMMENT '租户名称',
                             `log_level` varchar(40) DEFAULT '' COMMENT '日志级别',
                             `action_name` varchar(64) NOT NULL DEFAULT '' COMMENT '审计行为名称',
                             `detail` varchar(2000) NOT NULL DEFAULT '' COMMENT '审计日志详情',
                             `request_url` varchar(64) NOT NULL DEFAULT '' COMMENT '请求url',
                             `resource_type` varchar(64) NOT NULL DEFAULT '' COMMENT '资源类型',
                             `resource_name` varchar(64) NOT NULL DEFAULT '' COMMENT '资源名称',
                             `resource_id` varchar(40) NOT NULL DEFAULT '' COMMENT '审计对象资源id',
                             `result` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '访问结果，0失败，1成功',
                             `error_msg` varchar(1000) NOT NULL DEFAULT '' COMMENT '失败信息',
                             `action_type` varchar(64) NOT NULL DEFAULT '' COMMENT '审计类型',
                             `visitor_ip` varchar(64) NOT NULL DEFAULT '0.0.0.0' COMMENT '访问者ip',
                             `request_param` varchar(2000) NOT NULL DEFAULT '' COMMENT '请求入参',
                             `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
                             PRIMARY KEY (`id`),
                             KEY `audit_log_tenant_id_IDX` (`tenant_id`) USING BTREE,
                             KEY `audit_log_resource_id_IDX` (`resource_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4;