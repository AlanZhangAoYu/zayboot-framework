package cn.zay.zayboot.util;

import io.netty.handler.codec.http.QueryStringDecoder;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;

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
     * get the decoded path of uri
     */
    public static String getRequestPath(String uri) {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(uri, Charsets.toCharset(CharEncoding.UTF_8));
        return queryDecoder.path();
    }
}
