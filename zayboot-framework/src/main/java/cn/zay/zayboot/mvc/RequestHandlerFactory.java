package cn.zay.zayboot.mvc;
import cn.zay.zayboot.mvc.handler.GetRequestHandler;
import cn.zay.zayboot.mvc.handler.PostRequestHandler;
import cn.zay.zayboot.mvc.handler.RequestHandler;
import io.netty.handler.codec.http.HttpMethod;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZAY
 */
public class RequestHandlerFactory {
    /**
     * 单例模式
     */
    public static final Map<HttpMethod, RequestHandler> REQUEST_HANDLERS_MAP = new HashMap<>(2);
    static {
        REQUEST_HANDLERS_MAP.put(HttpMethod.GET, new GetRequestHandler());
        REQUEST_HANDLERS_MAP.put(HttpMethod.POST, new PostRequestHandler());
    }
    /**
     * 根据请求的不同分类返回不同的请求处理器 (RequestHandler)
     * @param httpMethod get/post
     * @return 请求处理器
     */
    public static RequestHandler get(HttpMethod httpMethod) {
        return REQUEST_HANDLERS_MAP.get(httpMethod);
    }
}
