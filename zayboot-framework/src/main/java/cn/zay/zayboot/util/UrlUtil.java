package cn.zay.zayboot.util;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 与 url编解码处理有关的工具
 * <p>Get请求 uri的格式可能有:</p>
 * <p>Restful格式: /user/getUserInfo/{xxxxxx}, 这个要与 @PathVariable配合</p>
 * <p>Restful格式: /user/{xxxxx}/{xxxxxx}, 这个要与 @PathVariable配合</p>
 * <p>常规格式: /user/getUserInfo?name=xxxxx&age=11</p>
 * <p>Post请求的 connect-type只支持 application/json格式</p>
 * @author ZAY
 */
public class UrlUtil {
    /**
     * 将 {xxx} 占位符替换为匹配中文、英文字母和数字以及下划线的正则表达式
     * 例子: "/user/{name}" 转换为: "^/user/[\u4e00-\u9fa5_a-zA-Z0-9]+/?$"
     * 解决了映射 RestFul风格的 url与 controller方法的问题, 参见:
     * GetRequestHandler.java中的注释
     */
    public static String formatUrl(String uri) {
        String originPattern = uri.replaceAll("(\\{\\w+})", "[\\\\u4e00-\\\\u9fa5_a-zA-Z0-9]+");
        String pattern = "^" + originPattern + "/?$";
        return pattern.replaceAll("/+", "/");
    }
    /**
     * 获取 uri的解码路径
     * @param uri 例如输入: /hello?recipient=world&x=1;y=2
     * @return 输出: /hello
     */
    public static String getRequestPath(String uri) {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(uri, Charsets.toCharset(CharEncoding.UTF_8));
        return queryDecoder.path();
    }

    /**
     * 获取 uri的参数键值对
     * @param uri 例如输入: /hello?recipient=world&x=1;y=2
     * @return 输出 {recipient=[world], x=[1], y=[2]}
     */
    private static Map<String, List<String>> getRequestParameters(String uri){
        QueryStringDecoder queryDecoder = new QueryStringDecoder(uri, Charsets.toCharset(CharEncoding.UTF_8));
        return queryDecoder.parameters();
    }
    /**
     * <p>从 uri中获取参数</p>
     * <p>Get请求</p>
     * <p>Restful格式: /user/getUserInfo/{xxxxxx}</p>
     * @param uri 传入的 uri地址与参数, 例如: "/user/1"
     * @param urlMap @GetMapping或 @PostMapping中设置的方法映射地址, 例如: "/user/{id}"
     * @return {"id" -> "1"}
     */
    public static Map<String, Object> getUrlParameterMapForRestFul(String uri, String urlMap) {
        String[] requestParams = uri.split("/");
        String[] urlParams = urlMap.split("/");
        Map<String, Object> urlParameterMap = new HashMap<>(urlParams.length);
        for (int i = 1; i < urlParams.length; i++) {
            urlParameterMap.put(urlParams[i].replace("{", "").replace("}", ""), requestParams[i]);
        }
        //这一句去掉了返回的结果中存在的多余的参数项
        urlParameterMap.entrySet().removeIf(entry -> entry.getKey().equals((String) entry.getValue()));
        return urlParameterMap;
    }
    /**
     * <p>从 uri中获取参数</p>
     * <p>Get请求</p>
     * <p>常规格式: /user/getUserInfo?name=xxxxx&age=11</p>
     * @param uri 要获取参数的 uri, 类似于: /hello?recipient=world&x=1;y=2
     * @return 参数名与参数值的映射
     */
    public static Map<String, Object> getUrlParameterMapForCommon(String uri) {
        //返回 URI的已解码键值参数对
        Map<String, List<String>> parameters = getRequestParameters(uri);
        Map<String, Object> resultMap = new HashMap<>(16);
        parameters.forEach((key,value) -> {
            for (String s : value) {
                resultMap.put(key, s);
            }
        });
        return resultMap;
    }

    /**
     * <p>从 uri的请求体中获取参数</p>
     * <p>Post请求</p>
     * <p>参考: https://blog.csdn.net/IUNIQUE/article/details/121654131</p>
     * @param request Netty封装的请求
     * @return 参数键值对映射
     */
    public static Map<String, Object> getPostRequestParams(FullHttpRequest request) {
        Map<String, Object> params = new HashMap<>(16);
        //对于 Content-Type为 application/json的类型获取请求体 body的信息
        String content = request.content().toString(CharsetUtil.UTF_8);
        HashMap<String,Object> deserialize = JsonUtil.deserialize(content, HashMap.class);
        if(deserialize != null){
            params.putAll(deserialize);
        }
        //对于 Content-Type为 x-www-form-urlencoded的类型, body返回的是一个如下的一个字符串: "key1=value1&key2=value2",
        //需要手动解析
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
        List<InterfaceHttpData> httpPostData = decoder.getBodyHttpDatas();
        for (InterfaceHttpData data : httpPostData) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            }
        }
        return params;
    }
}
