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
 * <p>扫描处理所有 @RestController uri方法, 将其放在 Map<HttpMethod,Map<String,Method>>中</p>
 * @author ZAY
 */
public class RouteMethodFactory {
    private static final HttpMethod[] HTTP_METHODS = {HttpMethod.GET, HttpMethod.POST};
    /**
     * <p>存放所有的 url与 Controller的映射</p>
     * <p>例如以下格式:</p>
     * {
     *     Get -> {
     *         "/student/getId" -> getIdController,
     *         "/student/getInfoByName/[\u4e00-\u9fa5_a-zA-Z0-9]+/?$" -> getInfoByNameController
     *     },
     *     Post -> {
     *         "/student/setId" -> setIdController,
     *         "/student/setName" -> setNameController
     *     }
     * }
     */
    private static final Map<HttpMethod, Map<String, Method>> REQUEST_METHOD_MAP = new HashMap<>(2);
    /**
     * 该 Map中存的是用 UriUtil.formatUrl()转换后的 uri与转换前的 uri的映射关系, 为了处理 RestFul格式的 uri真的做了太多太多……
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
            //获取 @RestController中设置的value值
            String baseUrl = restController.value();
            //获取当前 Controller类中的所有方法
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    GetMapping getMapping = method.getAnnotation(GetMapping.class);
                    if (getMapping != null) {
                        mapUrlToMethod(UrlUtil.formatUrl(baseUrl + getMapping.value()), method, HttpMethod.GET);
                        REQUEST_URL_MAP.get(HttpMethod.GET)
                                .put(UrlUtil.formatUrl(baseUrl + getMapping.value()),baseUrl + getMapping.value());
                    }
                }
                if (method.isAnnotationPresent(PostMapping.class)) {
                    PostMapping postMapping = method.getAnnotation(PostMapping.class);
                    if (postMapping != null) {
                        mapUrlToMethod(UrlUtil.formatUrl(baseUrl + postMapping.value()), method, HttpMethod.POST);
                        REQUEST_URL_MAP.get(HttpMethod.POST)
                                .put(UrlUtil.formatUrl(baseUrl + postMapping.value()),baseUrl + postMapping.value());
                    }
                }
            }
        }
    }
    /**
     * 将 url对应到对应的 Controller方法, 并存放到 REQUEST_METHOD_MAP中
     * @param uri 该方法对应的 url, 例如 /core/student/selectAllStudent或 /core/student/getStudent/{studentId}
     * @param method 要对应处理的方法
     * @param httpMethod 该方法为哪种请求方式, Get/Post
     */
    private static void mapUrlToMethod(String uri, Method method, HttpMethod httpMethod) {
        //根据传入的 httpMethod选择放入哪个 Map中
        Map<String, Method> urlToMethodMap = REQUEST_METHOD_MAP.get(httpMethod);
        if (urlToMethodMap.containsKey(uri)) {
            throw new IllegalArgumentException(String.format("存在重复的url: %s", uri));
        }
        urlToMethodMap.put(uri, method);
        REQUEST_METHOD_MAP.put(httpMethod, urlToMethodMap);
    }
    public static Map<HttpMethod, Map<String, Method>> getRequestMethodMap() {
        return REQUEST_METHOD_MAP;
    }
    public static Map<HttpMethod, Map<String, String>> getRequestUrlMap() {
        return REQUEST_URL_MAP;
    }
}
