package cn.zay.zayboot.mvc;

import cn.zay.zayboot.mvc.handler.GetRequestHandler;
import cn.zay.zayboot.mvc.handler.PostRequestHandler;
import cn.zay.zayboot.mvc.handler.RequestHandler;
import io.netty.handler.codec.http.HttpMethod;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shuang.kou
 * @createTime 2020年09月24日 14:28:00
 **/
public class RequestHandlerFactory {
    public static final Map<HttpMethod, RequestHandler> REQUEST_HANDLERS = new HashMap<>();
    static {
        REQUEST_HANDLERS.put(HttpMethod.GET, new GetRequestHandler());
        REQUEST_HANDLERS.put(HttpMethod.POST, new PostRequestHandler());
    }
    public static RequestHandler get(HttpMethod httpMethod) {
        return REQUEST_HANDLERS.get(httpMethod);
    }
}
