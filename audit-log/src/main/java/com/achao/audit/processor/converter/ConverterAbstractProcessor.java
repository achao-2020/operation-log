package com.achao.audit.processor.converter;

import cn.hutool.core.bean.BeanUtil;
import com.achao.audit.annotation.AuditColumn;
import com.achao.audit.common.ColumnType;
import com.achao.audit.common.DesType;
import com.achao.audit.convert.IAuditConvert;
import com.achao.audit.desensitize.BasePattern;
import com.achao.audit.entity.AuditSaveEntity;
import com.achao.audit.metedata.AuditLogMetadata;
import com.achao.audit.metedata.convert.ConvertMetaData;
import com.achao.audit.processor.IAuditProcessor;
import com.achao.audit.service.IAuditSaveService;
import com.achao.audit.util.AuditThreadLocal;
import com.achao.audit.util.SpringContextUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 转换类审计日志处理器抽象类
 *
 * @author achao
 */
public abstract class ConverterAbstractProcessor implements IAuditProcessor {

    protected Logger logger = LoggerFactory.getLogger(ConverterAbstractProcessor.class);

    protected IAuditSaveService auditSaveService;

    private Map<String, BasePattern> desMap;

    public ConverterAbstractProcessor(IAuditSaveService auditSaveService, Map<String, BasePattern> desMap) {
        this.auditSaveService = auditSaveService;
        this.desMap = desMap;
    }

    /**
     * 解析审计日志
     *
     * @param auditLogMetadata
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public AuditSaveEntity resolveAuditLog(AuditLogMetadata auditLogMetadata) throws IllegalAccessException, NoSuchFieldException {
        AuditSaveEntity saveEntity = new AuditSaveEntity();
        ConvertMetaData convertMetadata = auditLogMetadata.getConvertMetadata();
        // 实现了自定义wrapper
        if (!ObjectUtils.isEmpty(convertMetadata) && ObjectUtils.notEqual(convertMetadata.getConvertClass(), IAuditConvert.class)) {
            // 获取审计包装类
            IAuditConvert wrapper = convertWrapper(auditLogMetadata);
            // 渲染日志细节
            renderDetail(saveEntity, wrapper, auditLogMetadata);
            // 额外日志信息
            auditExtraAttribute(saveEntity, wrapper, auditLogMetadata);
        }
        return auditSaveService.saveAuditLog(saveEntity, auditLogMetadata);
    }

    /**
     * 渲染日志细节
     *
     * @param saveDto
     * @param convert
     * @param auditLogMetadata
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    protected abstract void renderDetail(AuditSaveEntity saveDto, IAuditConvert convert, AuditLogMetadata auditLogMetadata) throws IllegalAccessException, NoSuchFieldException
    ;

    /**
     * 保存日志额外的信息
     *
     * @param saveDto
     * @param wrapper
     * @param auditLogMetadata
     */
    protected abstract void auditExtraAttribute(AuditSaveEntity saveDto, IAuditConvert wrapper, AuditLogMetadata auditLogMetadata) throws IllegalAccessException;


    /**
     * 默认包装类转换
     *
     * @param auditLogMetadata
     * @return
     */
    protected IAuditConvert convertWrapper(AuditLogMetadata auditLogMetadata) {
        ConvertMetaData convertMetadata = auditLogMetadata.getConvertMetadata();
        Object[] params = convertMetadata.getRequestParam();
        Class<IAuditConvert> convertSuperClass = convertMetadata.getConvertClass();
        IAuditConvert convert = null;
        for (Object param : params) {
            if (param.getClass().equals(convertSuperClass.getSuperclass())) {
                convert = BeanUtil.toBean(param, convertSuperClass);
            }
        }
        if (Objects.isNull(convert)) {
            logger.error("审计日志解析wrapper类型失败：wrapperClass:", convertSuperClass.getName());
        }
        return convert;
    }

    /**
     * 审计字段赋值
     *
     * @param wrapper
     */
    protected void invokePostConvert(IAuditConvert wrapper, AuditLogMetadata auditLogMetadata)  {
        // 获取包装类的其他属性
        try {
            Class<? extends IAuditConvert> convertClass = wrapper.getClass();
            assembleBean(wrapper, auditLogMetadata);
            Method wrapperMethod = convertClass.getMethod("postWrapper");
            wrapperMethod.invoke(wrapper);
        }catch (Exception e) {
            logger.error("postWrapper方法执行错误，请检查！");
        }
    }

    /**
     * 往convert中注入service bean
     *
     * @param wrapper
     * @throws IllegalAccessException
     */
    protected void assembleBean(IAuditConvert wrapper, AuditLogMetadata auditLogMetadata) {
        // 注入serviceBean
        try {
            Class<? extends IAuditConvert> convertClass = wrapper.getClass();
            Field[] fields = convertClass.getDeclaredFields();
            for (Field field : fields) {
                Class<?> beanClass = field.getType();
                // 注入bean
                Object bean = SpringContextUtil.getBean(beanClass);
                if (Objects.nonNull(bean)) {
                    field.setAccessible(true);
                    field.set(wrapper, bean);
                }
                if (beanClass.equals(AuditLogMetadata.class)) {
                    field.setAccessible(true);
                    field.set(wrapper, auditLogMetadata);
                }
            }
        } catch (Exception e) {
            logger.error("包装类注入bean失败");
        }
    }

    /**
     * 拼接wrapper审计字段内容
     * @param convert
     * @return
     * @throws IllegalAccessException
     */
    protected StringBuffer convertDetails(IAuditConvert convert) throws IllegalAccessException {
        StringBuffer detail = new StringBuffer();
        List<Field> fieldList = allFields(convert);
        int count = 0;
        for (Field field : fieldList) {
            field.setAccessible(true);
            AuditColumn auditColumn = field.getAnnotation(AuditColumn.class);
            if (Objects.nonNull(auditColumn) && auditColumn.isAudit()) {
                count ++;
                String name = auditColumn.name();
                // 字符串检验？
                Object value = desParse(field.get(convert), auditColumn.dstBean());
                if (ObjectUtils.isEmpty(value)) {
                    value = "无";
                }
                detail.append(name).append("：").append(value);
                if (count % 3 == 0) {
                    // 每三个审计字段换行
                    detail.append("<br />");
                } else {
                    detail.append("，");
                }
            }
        }
        return detail;
    }

    /**
     * 获得所有字段包含父类
     *
     * @param convert
     * @return
     */
    protected List<Field> allFields(IAuditConvert convert) {
        Class wrapperClass = convert.getClass();
        Field[] fields = wrapperClass.getDeclaredFields();
        Field[] superFields = wrapperClass.getSuperclass().getDeclaredFields();
        List<Field> rtFields = new ArrayList<>(fields.length + superFields.length);
        for (Field field : fields) {
            rtFields.add(field);
        }
        for (Field field : superFields) {
            rtFields.add(field);
        }
        return rtFields;
    }


    /**
     * 字段脱敏
     * @param value
     * @param desType
     * @return
     */
    protected Object desParse(Object value, DesType desType) {
        if (value instanceof String) {
            BasePattern pattern = desMap.get(desType.getBeanName());
            if (pattern != null) {
                return pattern.desData((String) value);
            } else if (!desType.equals(DesType.DST_NULL)){
                logger.warn("无法进行脱敏！");
            }
        }
        return value;
    }

    /**
     * 获得资源id、资源名称值
     *
     */
    protected void injectResourceReflect(IAuditConvert convert, AuditSaveEntity saveEntity) throws IllegalAccessException {
        Object resourceId = null;
        Object resourceName = null;
        List<Field> fieldList = allFields(convert);
        for (Field field : fieldList) {
            AuditColumn auditColumn = field.getDeclaredAnnotation(AuditColumn.class);
            if (Objects.nonNull(auditColumn)) {
                field.setAccessible(true);
                if (auditColumn.columnType().equals(ColumnType.ID)) {
                    resourceId = field.get(convert);
                    saveEntity.setResourceId((String) resourceId);
                } else if (auditColumn.columnType().equals(ColumnType.NAME)) {
                    resourceName = field.get(convert);
                    saveEntity.setResourceName((String) resourceName);
                }
            }
        }
        if (ObjectUtils.isEmpty(resourceId)) {
            AuditLogMetadata auditLogMetadata = AuditThreadLocal.get();
            if (StringUtils.isNotEmpty(auditLogMetadata.getResourceId())) {
                saveEntity.setResourceId(auditLogMetadata.getResourceId());
            } else {
                logger.debug("资源对象id不存在");
            }
        }
        if (ObjectUtils.isEmpty(resourceName)) {
            logger.debug("资源对象name不存在");
        }
    }
}
