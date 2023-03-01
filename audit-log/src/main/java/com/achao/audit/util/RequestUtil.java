package com.achao.audit.util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author achao
 */
public class RequestUtil {

    /**
     * 定义获取 ip 的 header
     */
    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR" };

    /**
     * 获取 IP 地址
     * @param request HttpServletRequest
     * @return IP
     */
    public static String getIpAddr(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (null != ip && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        String ip = request.getRemoteAddr();
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 获取请求地址url
     * @param method
     * @return
     */
    public static String getRestUrl(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        RequestMapping requestMapping = declaringClass.getAnnotation(RequestMapping.class);
        String apiPrefix = "";
        if (!Objects.isNull(requestMapping)) {
            String[] value = requestMapping.value();
            apiPrefix = requestMapping != null ? Arrays.toString(ObjectUtils.isEmpty(value)?requestMapping.path():value) : "";
        }
        Annotation[] annotations = method.getAnnotations();
        String api = "";
        for (Annotation annotation : annotations) {
            String annotationName = annotation.toString().substring(annotation.toString().lastIndexOf('.'));
            if (annotationName.contains("PostMapping")) {
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                api = comboUrl(apiPrefix, postMapping.value());
            }
            if (annotationName.contains("PutMapping")) {
                PutMapping putMapping = method.getAnnotation(PutMapping.class);
                api = comboUrl(apiPrefix, putMapping.value());
            }
            if (annotationName.contains("DeleteMapping")) {
                DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
                api = comboUrl(apiPrefix, deleteMapping.value());
            }
            if (annotationName.contains("GetMapping")) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                api = comboUrl(apiPrefix, getMapping.value());
            }
            if (annotationName.contains("RequestMapping")) {
                RequestMapping reqMapping = method.getAnnotation(RequestMapping.class);
                api = comboUrl(apiPrefix, reqMapping.value());
            }
        }
        api = api.replaceAll("[\\[|\\]]", "");
        return api;
    }

    /**
     * 过滤请求路径中多余的“/”
     * @param apiPrefix
     * @param value
     * @return
     */
    private static String comboUrl(String apiPrefix, String[] value) {
        String api = Arrays.toString(value).replaceAll("[\\[|\\]]", "");
        apiPrefix = apiPrefix.replaceAll("[\\[|\\]]", "");
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotEmpty(apiPrefix) && !apiPrefix.startsWith("/")) {
            sb.append("/");
        }
        if ((apiPrefix.endsWith("/")&&!api.startsWith("/")) || (!apiPrefix.endsWith("/")&&api.startsWith("/"))) {
            if (api.endsWith("/")) {
                api = api.substring(0, api.length()-1);
            }
            sb.append(apiPrefix).append(api);
        } else if (!apiPrefix.endsWith("/") && !api.startsWith("/")) {
            if (api.endsWith("/")) {
                api = api.substring(0, api.length()-1);
            }
            sb.append(apiPrefix).append("/").append(api);
        } else {
            if (api.endsWith("/")) {
                api = api.substring(0, api.length()-1);
            }
            apiPrefix = apiPrefix.substring(0, apiPrefix.length()-1);
            sb.append(apiPrefix).append(api);
        }
        return sb.toString();
    }

    public static String parseJsonBody(Object[] args) {
        try {
            if (args != null && args.length != 0 && args[0] instanceof MultipartFile) {
                return "MultipartFile";
            }
            return JSON.toJSONString(args, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            return "";
        }
    }
}
