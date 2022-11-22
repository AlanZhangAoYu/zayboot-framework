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
     * 该 Map中存的是类似以下格式的数据
     * {POST={^/student/setStudentInfo/?$=public java.lang.String cn.zay.demo.java.controller.StudentController.setStudentInfo()},
     *  GET={^/student/getStudentInfo/?$=public java.lang.String cn.zay.demo.java.controller.StudentController.getStudentInfo()}}
     * key : http方法, value : url -> method
     */
    private static final Map<HttpMethod, Map<String, Method>> REQUEST_METHOD_MAP = new HashMap<>(2);
    /**
     * 该 Map中存的是类似以下格式的数据
     * {POST={^/student/setStudentInfo/?$=/student/setStudentInfo},
     *  GET={^/student/getStudentInfo/?$=/student/getStudentInfo}}
     * key : http方法, value : 格式化后的 url -> 原始的 url
     */
    private static final Map<HttpMethod, Map<String, String>> REQUEST_URL_MAP = new HashMap<>(2);
    static {
        for (HttpMethod httpMethod : HTTP_METHODS) {
            REQUEST_METHOD_MAP.put(httpMethod, new HashMap<>(128));
            REQUEST_URL_MAP.put(httpMethod, new HashMap<>(128));
        }
    }
    /**
     * 获取被 @RestController注解的类并将其中的 get和 post方法处理并存放到 REQUEST_METHOD_MAP与 REQUEST_URL_MAP中
     */
    public static void loadRoutes() {
        //获取所有被 @RestController注释的类
        Set<Class<?>> classes = BeanFactory.CLASSES.get(RestController.class);
        for (Class<?> aClass : classes) {
            RestController restController = aClass.getAnnotation(RestController.class);
            //获取 @RestController中设置的value值 (前置url)
            String baseUrl = restController.value();
            //获取当前 Controller类中的所有方法
            Method[] methods = aClass.getDeclaredMethods();
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
    /**
     * 通过请求路径 (例如 /student)和请求类型获取封装好的方法
     * @param requestPath
     * @param httpMethod
     * @return
     */
    public static MappingMethodDetail getMethodDetail(String requestPath, HttpMethod httpMethod) {
        MappingMethodDetail mappingMethodDetail = new MappingMethodDetail();
        mappingMethodDetail.build(requestPath, REQUEST_METHOD_MAP.get(httpMethod), REQUEST_URL_MAP.get(httpMethod));
        return mappingMethodDetail;
    }
    /**
     * 将 url对应到方法
     * @param uri 该方法对应的 url, 例如 /core/student/selectAllStudent或 /core/student/getStudent/{studentId}
     * @param method 要对应处理的方法
     * @param httpMethod 该方法为哪种请求方式, Get/Post
     */
    private static void mapUrlToMethod(String uri, Method method, HttpMethod httpMethod) {
        String formattedUrl = UrlUtil.formatUrl(uri);
        Map<String, Method> urlToMethodMap = REQUEST_METHOD_MAP.get(httpMethod);
        Map<String, String> formattedUrlToUrlMap = REQUEST_URL_MAP.get(httpMethod);
        if (urlToMethodMap.containsKey(formattedUrl)) {
            throw new IllegalArgumentException(String.format("duplicate url: %s", uri));
        }
        urlToMethodMap.put(formattedUrl, method);
        formattedUrlToUrlMap.put(formattedUrl, uri);
        REQUEST_METHOD_MAP.put(httpMethod, urlToMethodMap);
        REQUEST_URL_MAP.put(httpMethod, formattedUrlToUrlMap);
    }
    public static Map<HttpMethod, Map<String, Method>> getRequestMethodMap() {
        return REQUEST_METHOD_MAP;
    }
    public static Map<HttpMethod, Map<String, String>> getRequestUrlMap() {
        return REQUEST_URL_MAP;
    }
}
