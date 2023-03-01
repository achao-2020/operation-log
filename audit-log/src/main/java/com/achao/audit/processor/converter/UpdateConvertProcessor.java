package com.achao.audit.processor.converter;

import com.achao.audit.annotation.AuditColumn;
import com.achao.audit.common.ColumnType;
import com.achao.audit.convert.IAuditConvert;
import com.achao.audit.desensitize.BasePattern;
import com.achao.audit.entity.AuditSaveEntity;
import com.achao.audit.metedata.AuditLogMetadata;
import com.achao.audit.service.IAuditSaveService;
import com.achao.audit.util.ConvertUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author achao
 */
public class UpdateConvertProcessor extends ConverterAbstractProcessor {
    public UpdateConvertProcessor(IAuditSaveService auditSaveService, Map<String, BasePattern> desMap) {
        super(auditSaveService, desMap);
    }

    @Override
    protected void renderDetail(AuditSaveEntity saveEntity, IAuditConvert convert, AuditLogMetadata auditLogMetadata) throws IllegalAccessException, NoSuchFieldException {
        if (StringUtils.isEmpty(saveEntity.getResourceId())) {
            injectResourceReflect(convert, saveEntity);
        }
        // 获取上一次入参wrapper
        AuditSaveEntity lastSaveEntity = auditSaveService.recentlyUpdateLog(saveEntity.getTenantId(), saveEntity.getResourceId());
        if (Objects.isNull(lastSaveEntity)) {
            logger.warn("无法获取上一次审计日志: appId:{}, resourceId:{}", saveEntity.getTenantId(), saveEntity.getResourceId());
            // 无法获取上一次入参，则直接输出此次更改日志
            invokePostConvert(convert, auditLogMetadata);
            StringBuffer wrapperDetail = convertDetails(convert);
            saveEntity.setDetail(wrapperDetail.toString());
            return;
        }
        IAuditConvert lastWrapper = parseLastConvert(lastSaveEntity, convert.getClass(), saveEntity.getResourceId());
        // 执行"postWrapper"方法
        invokePostConvert(lastWrapper, auditLogMetadata);
        invokePostConvert(convert, auditLogMetadata);

        renderDifDetail(saveEntity, convert, lastWrapper);
    }

    /**
     * 前后对比，审计渲染
     *
     * @param saveEntity
     * @param convert
     * @param lastWrapper
     */
    private void renderDifDetail(AuditSaveEntity saveEntity, IAuditConvert convert, IAuditConvert lastWrapper) {
        // 比较两个类, 先判断需要审计的字段，后前后对比，格式化信息输出
        StringBuffer detail = new StringBuffer();
        try {
            // 得到类对象
            Class<? extends IAuditConvert> lastClass = lastWrapper.getClass();
            Class<? extends IAuditConvert> currentClass = convert.getClass();
            // 得到属性集合
            Field[] fields1 = lastClass.getDeclaredFields();
            Field[] fields2 = currentClass.getDeclaredFields();
            int i = 1;
            for (Field field1 : fields1) {
                // 设置属性是可以访问的(私有的也可以)
                field1.setAccessible(true);
                for (Field field2 : fields2) {
                    // 设置属性是可以访问的(私有的也可以)
                    field2.setAccessible(true);
                    // 比较属性名是否一样
                    if (field1.equals(field2)) {
                        if (ObjectUtils.isEmpty(field2.get(convert))) {
                            break;
                        }
                        // 比较属性值是否一样
                        if (ObjectUtils.notEqual(field1.get(lastWrapper), field2.get(convert))) {
                            // 得到注解
                            AuditColumn auditColumn = field1.getAnnotation(AuditColumn.class);
                            if (auditColumn != null && auditColumn.isAudit()) {
                                if (i != 1 && i % 3 != 0) {
                                    detail.append("，");
                                }
                                if (i % 3 == 0) {
                                    // 每三个换行
                                    detail.append("<br />");
                                }
                                detail.append(auditColumn.name()).append("：\"").append(desParse(field1.get(lastWrapper), auditColumn.dstBean()))
                                        .append("\" 改成 \"").append(desParse(field2.get(convert), auditColumn.dstBean())).append("\"");
                                i++;
                            }
                        }
                        // 属性名称一样就退出二级循环
                        break;
                    }
                }
            }
            if (StringUtils.isNotEmpty(detail)) {
                // 去除最后一个“，”
                saveEntity.setDetail(detail.substring(0, detail.length()));
            } else {
                saveEntity.setDetail(detail.append("无变更").toString());
            }
        } catch (Exception e) {
            logger.error("属性内容更改前后验证错误,日志无法被记录！");
        }
    }

    /**
     * 审计日志入参转化wrapper
     *
     * @param lastSaveEntity
     * @param aClass
     * @param resourceId
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private IAuditConvert parseLastConvert(AuditSaveEntity lastSaveEntity, Class<? extends IAuditConvert> aClass, String resourceId) throws NoSuchFieldException, IllegalAccessException {
        String param = lastSaveEntity.getRequestParam();

        Object parseObject = JSONObject.parseObject(param, Object.class);
        if (parseObject instanceof JSONArray) {
            for (Object p : ((JSONArray) parseObject)) {
                return ConvertUtil.jsonObjectToWrapper((JSONObject) p, aClass);
            }
        }
        return null;
    }

    /**
     * 获取资源id的字段名称
     *
     * @param aClass
     * @return
     */
    private String getResourceIdField(Class<? extends IAuditConvert> aClass) {
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            AuditColumn auditColumn = field.getDeclaredAnnotation(AuditColumn.class);
            if (Objects.nonNull(auditColumn) && auditColumn.columnType().equals(ColumnType.ID)) {
                return field.getName();
            }
        }
        return null;
    }

    @Override
    protected void auditExtraAttribute(AuditSaveEntity saveDto, IAuditConvert wrapper, AuditLogMetadata auditLogMetadata) throws IllegalAccessException {
        injectResourceReflect(wrapper, saveDto);
    }
}
