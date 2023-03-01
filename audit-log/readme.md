## 一、审计日志的背景

&emsp;&emsp;每个系统的开发必定绕不开权限的设计，必定也就绕不开权限的管理。在这样的情况下，拥有高权限的用户的每一次操作都有可能对下游的系统产生不可预估的权限问题。为了方便回溯，方便安全部门审计用户行为，则需要在每个权限的管理端，对用户的操作行为进行记录——这就是审计日志。

## 二、从企业的角度看审计行为

&emsp;&emsp;一般来说，审计的要求是从安全的角度考虑的，从一个系统的开发到上线，安全部门都需要管控该系统的权限模型是否能够提供给他们做安全审计。一般来说，安全部门的审计主要考虑**两个方面**：一是审计用户的访问是否符合该系统的权限设计，二是用户的访问是否拥有威胁行为。具体可以看下图的审计模型示例：

![安全审计.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/0c4363d9182a45b683e025e8f09c0ad2~tplv-k3u1fbpfcp-watermark.image?)

&emsp;&emsp;这篇文章关心的是【记录用户访问行为】的功能实现，也就是认证授权后的记录行为，主要的难点在于如何记录用户的详细操作。

## 三、审计日志的架构设计

### 3.1 审计内容

&emsp;&emsp;一般的审计日志可能直接在拦截器中做用户审计记录，这种权限模型需要将url关联到权限点，直接记录用户信息和权限点。但是缺少了【详细信息】——用户的具体操作行为。所以，采用**AOP+注解**的方式，可以直接获取到入参类，且可以定制更多的入参信息。</br>
&emsp;&emsp;最后期望的审计日志记录效果如下表所示：</br>

### 3.2 架构设计

![审计架构设计.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/98117cd330f140b19ab64b9151a766a2~tplv-k3u1fbpfcp-watermark.image?)

**架构说明**
</br>
&emsp;&emsp;需要审计的方法会被aop拦截，获取这次请求的路径，参数，IP, 访问用户的基本信息，放入到本地线程变量当中（同步操作）。删除操作，需要在删除前存储审计日志的结果，避免多线程的交互，直接使用同步的方式；新增操作，更新操作，都是使用异步的方式渲染审计日志。其中，更新操作需要获取上次审计对象的内容，做”操作内容对比“渲染输出。</br>
&emsp;&emsp;

## 四、模块设计

### 审计注解

@AuditColumn

| 属性名        | 字段类型    | 描述     | 是否必须 | 默认值               |
| ---------- | ------- | ------ | ---- | ----------------- |
| name       | string  | 审计字段名称 | Y    |                   |
| isAudit    | boolean | 是否审计   | N    | true              |
| columnType | emun    | 字段类型   | N    | ColumnType.NORMAL |
| desType    | emun    | 脱敏类型   | N    | DesType.DST_NULL  |

@AuditLog

| 属性名          | 字段类型          | 描述                                            | 是否必须 | 默认值             |
| ------------ | ------------- | --------------------------------------------- | ---- | --------------- |
| actionType   | enum          | 操作类型                                          | Y    |                 |
| logLevel     | enum          | 审计日志级别                                        | N    | LogLevel.NORMAL |
| actionName   | string        | 操作名称，当前接口可以描述的操作名称                            | N    | “”              |
| resourceType | string        | 资源类型，可以关联使用方的权限模型唯一值                          | N    | unknown         |
| springEl     | string        | SpEl表达式，快速输出审计内容                              | N    | ""              |
| convertClass | IAuditConvert | 审计日志变更类，如果springEl不为空，则优先以springEl模板的方式输出渲染内容 | N    | IAuditConvert   |

### 审计convert类

    该类作为操作日志的详情输出，需要对每个字段进行定制化，实现IAuditConvert接口：如下代码：

```java
@Data
public class OrganizationSaveConvert extends OrganizationSaveParam implements IAuditConvert<OrganizationSaveConvert> {
    // 会自动注入
    private OrgTypeService orgTypeService;

    @AuditColumn(name = "组织名称", columnType = ColumnType.NAME, dstBean = DesType.DES_NAME)
    private String orgName;

    @AuditColumn(name = "父组织id")
    private String parentId;

    @AuditColumn(name = "组织类型名称")
    private String orgTypeName;

    @Override
    public OrganizationSaveConvert preWrapper(Map<String, Object> map) {
        // 在接口方法执行前调用，可以用来查询删除的对象信息
        return this;
    }

    @Override
    public OrganizationSaveConvert postWrapper() {
        // 在接口方法执行后调用，可以用来更新，编辑查询对象信息
        this.orgTypeName = orgTypeService.getById(super.getOrgTypeId()).getOrgTypeName();
        return this;
    }
}  return this;
    }
}
```



### UML图

<img title="" src="file:///C:/Users/98537/AppData/Roaming/marktext/images/2023-01-02-23-22-33-未命名文件.png" alt="" data-align="center">



### 脱敏设计

代码包路径`com.achao.audit.desensitize`

| 脱敏名称 | 类型           | 示例                               |
| ---- | ------------ | -------------------------------- |
| 姓名   | DES_NAME     | 张三：张*                            |
| 电话   | DES_PHONE    | 11200002222：112****2222          |
| 邮件   | DES_EMAIL    | 123456789@qq.com: 12*****@qq.com |
| 生日   | DES_BIRTHDAY | 1971 02 02: 1971 ****            |
| .... |              |                                  |

 

### 审计元数据

代码包路径：com.achao.audit.metadata

### 审计效果

包路径：com/achao/org/controller/OrganizationController

效果：

![](C:\Users\98537\AppData\Roaming\marktext\images\2022-12-28-23-10-20-image.png)
