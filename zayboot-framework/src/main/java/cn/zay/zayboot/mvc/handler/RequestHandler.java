package cn.zay.zayboot.mvc.handler;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * Netty服务器的处理 请求地址与后端方法 的处理器
 * @author ZAY
 */
public interface RequestHandler {
    /**
     * 请求的接收与处理并返回响应
     * @param fullHttpRequest Netty封装的请求体
     * @throws Exception 可能返回资源未找到异常
     * @return Netty封装的响应体
     */
    FullHttpResponse handle(FullHttpRequest fullHttpRequest) throws Exception;
}
