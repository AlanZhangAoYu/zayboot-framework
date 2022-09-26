package cn.zay.zayboot.mvc.handler;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @author ZAY
 */
public interface RequestHandler {
    /**
     *
     * @param fullHttpRequest
     * @return
     */
    FullHttpResponse handle(FullHttpRequest fullHttpRequest);
}
