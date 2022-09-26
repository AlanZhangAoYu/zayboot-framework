package cn.zay.zayboot.mvc;

import cn.zay.zayboot.annotation.springmvc.GetMapping;
import cn.zay.zayboot.annotation.springmvc.PostMapping;
import cn.zay.zayboot.annotation.springmvc.RestController;
import cn.zay.zayboot.core.ioc.BeanFactory;
import cn.zay.zayboot.util.UrlUtil;
import io.netty.handler.codec.http.HttpMethod;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 扫描处理所有 @RestController uri方法
 * @author ZAY
 */
public class RouteMethodFactory {
    public static final HttpMethod[] HTTP_METHODS = {HttpMethod.GET, HttpMethod.POST};
    /**
     * key : http方法, value : url -> method
     */
    private static final Map<HttpMethod, Map<String, Method>> REQUEST_METHOD_MAP = new HashMap<>(2);
    /**
     * key : http方法, value : 格式化后的 url -> 原始的 url
     */
    private static final Map<HttpMethod, Map<String, String>> REQUEST_URL_MAP = new HashMap<>(2);
    static {
        for (HttpMethod httpMethod : HTTP_METHODS) {
            REQUEST_METHOD_MAP.put(httpMethod, new HashMap<>(128));
            REQUEST_URL_MAP.put(httpMethod, new HashMap<>(128));
        }
    }
    public static void loadRoutes() {
        Set<Class<?>> classes = BeanFactory.CLASSES.get(RestController.class);
        for (Class<?> aClass : classes) {
            RestController restController = aClass.getAnnotation(RestController.class);
            if (null != restController) {
                Method[] methods = aClass.getDeclaredMethods();
                String baseUrl = restController.value();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        GetMapping getMapping = method.getAnnotation(GetMapping.class);
                        if (getMapping != null) {
                            mapUrlToMethod(baseUrl + getMapping.value(), method, HttpMethod.GET);
                        }
                    }
                    if (method.isAnnotationPresent(PostMapping.class)) {
                        PostMapping postMapping = method.getAnnotation(PostMapping.class);
                        if (postMapping != null) {
                            mapUrlToMethod(baseUrl + postMapping.value(), method, HttpMethod.POST);
                        }
                    }
                }
            }
        }
    }
    public static MappingMethodDetail getMethodDetail(String requestPath, HttpMethod httpMethod) {
        MappingMethodDetail mappingMethodDetail = new MappingMethodDetail();
        mappingMethodDetail.build(requestPath, REQUEST_METHOD_MAP.get(httpMethod), REQUEST_URL_MAP.get(httpMethod));
        return mappingMethodDetail;
    }
    /**
     * correspond url to method
     */
    private static void mapUrlToMethod(String url, Method method, HttpMethod httpMethod) {
        String formattedUrl = UrlUtil.formatUrl(url);
        Map<String, Method> urlToMethodMap = REQUEST_METHOD_MAP.get(httpMethod);
        Map<String, String> formattedUrlToUrlMap = REQUEST_URL_MAP.get(httpMethod);
        if (urlToMethodMap.containsKey(formattedUrl)) {
            throw new IllegalArgumentException(String.format("duplicate url: %s", url));
        }
        urlToMethodMap.put(formattedUrl, method);
        formattedUrlToUrlMap.put(formattedUrl, url);
        REQUEST_METHOD_MAP.put(httpMethod, urlToMethodMap);
        REQUEST_URL_MAP.put(httpMethod, formattedUrlToUrlMap);
    }
}
