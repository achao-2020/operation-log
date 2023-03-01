package com.achao.audit.util;

import com.achao.audit.convert.IAuditConvert;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * 包装类属性赋值工具类
 */
public class ConvertUtil {

    private static final Logger logger = LoggerFactory.getLogger(ConvertUtil.class);

    /**
     * json转化成为convert，用于入参转换成为convert
     * @param jsonObject
     * @param convertClass
     * @return
     */
    public static IAuditConvert jsonObjectToWrapper(JSONObject jsonObject, Class<? extends IAuditConvert> convertClass) {
        IAuditConvert wrapper = null;
        try {
            wrapper = convertClass.newInstance();
            List<Field> fieldList = allFields(convertClass);
            for (Field field : fieldList) {
                if (ObjectUtils.isNotEmpty(jsonObject.get(field.getName()))) {
                    field.setAccessible(true);
                    field.set(wrapper, convert(field.getType(), jsonObject.get(field.getName())));
                }
            }
        } catch (Exception e) {
            logger.error("jsonObjectToWrapper出错！", e);
        }
        return wrapper;
    }

    private static Object convert(Class<?> type, Object o) {
        if (type == Byte.class ) {
            return Byte.valueOf(String.valueOf(o));
        }
        if (type == Short.class) {
            return Short.valueOf(String.valueOf(o));
        }
        if (type == BigDecimal.class) {
            return BigDecimal.valueOf(Long.valueOf(String.valueOf(o)));
        }
        if (type == Integer.class) {
            return Integer.valueOf(String.valueOf(o));
        }
        if (type == Date.class && o.getClass() == Long.class) {
            if (!Objects.isNull(o)) {
                return new Date((Long) o);
            }
        }
        return o;
    }

    /**
     * 获取当前类继承的父类和自身的字段(包括私有继承)
     * @param obj
     * @return
     */
    public static List<Field> allFields (Class obj) {
        Field[] fields = obj.getDeclaredFields();
        List<Field> fieldList = new ArrayList<>();
        Collections.addAll(fieldList, fields);
        Class<?> superclass = obj.getSuperclass();
        if (!Objects.isNull(superclass)) {
            Field[] declaredFields = superclass.getDeclaredFields();
            for (Field field : declaredFields) {
                fieldList.add(field);
            }
        }
        return fieldList;
    }
}
