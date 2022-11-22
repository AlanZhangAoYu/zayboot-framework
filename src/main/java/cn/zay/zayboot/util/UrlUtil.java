package cn.zay.zayboot.util;

import io.netty.handler.codec.http.QueryStringDecoder;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;

import java.util.List;
import java.util.Map;

/**
 * 与 url编解码处理有关的工具
 * @author ZAY
 */
public class UrlUtil {
    /**
     * 将 {xxx} 占位符替换为匹配中文、英文字母和数字以及下划线的正则表达式
     * 例子: "/user/{name}" 转换为: "^/user/[\u4e00-\u9fa5_a-zA-Z0-9]+/?$"
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
    public static Map<String, List<String>> getRequestParameters(String uri){
        QueryStringDecoder queryDecoder = new QueryStringDecoder(uri, Charsets.toCharset(CharEncoding.UTF_8));
        return queryDecoder.parameters();
    }
}
