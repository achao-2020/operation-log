# 基于spring mvc后端接口操作日志



## 背景

> 目前存在的操作日志，无非从网关，拦截器等入手，虽然能够记录下用户的访问行为，也能进行越权操作，但是无法对用户操作的详细内容进行审计。后面出现了问题无法追溯与定责。



## 操作日志案例

> 常见的后端接口无非是增删改查，复杂的逻辑会有批量list的数据处理。本案例教程通过简单组织管理后端接口，演示增删改查接口的操作日志使用的输出样例。
