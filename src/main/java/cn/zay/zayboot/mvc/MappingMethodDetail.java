package cn.zay.zayboot.mvc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 封装 uri映射与具体 Java方法
 * @author ZAY
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MappingMethodDetail {
    private Method method;
    private Map<String, String> urlParameterMappings;
    private Map<String, String> queryParameterMappings;
    private String json;
    public void build(String requestPath, Map<String, Method> requestMappings, Map<String, String> urlMappings) {
        requestMappings.forEach((key, value) -> {
            //创建匹配 key的正则表达式的匹配模式
            Pattern pattern = Pattern.compile(key);
            if (pattern.matcher(requestPath).find()) {
                this.setMethod(value);
                String url = urlMappings.get(key);
                Map<String, String> urlParameterMappings = getUrlParameterMappings(requestPath, url);
                this.setUrlParameterMappings(urlParameterMappings);
            }
        });
    }
    /**
     * 将请求路径参数与 URL参数匹配
     * @param requestPath 传入的 uri地址与参数, 例如: "/user/1"
     * @param url @GetMapping或 @PostMapping中设置的方法映射地址, 例如: "/user/{id}"
     * @return {"id" -> "1","user" -> "user"}
     */
    private Map<String, String> getUrlParameterMappings(String requestPath, String url) {
        String[] requestParams = requestPath.split("/");
        String[] urlParams = url.split("/");
        Map<String, String> urlParameterMappings = new HashMap<>(urlParams.length);
        for (int i = 1; i < urlParams.length; i++) {
            urlParameterMappings.put(urlParams[i].replace("{", "").replace("}", ""), requestParams[i]);
        }
        return urlParameterMappings;
    }
}
