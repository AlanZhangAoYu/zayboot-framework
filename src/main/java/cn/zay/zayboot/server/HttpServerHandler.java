package cn.zay.zayboot.server;

import cn.zay.zayboot.mvc.FullHttpResponseFactory;
import cn.zay.zayboot.mvc.RequestHandlerFactory;
import cn.zay.zayboot.mvc.handler.RequestHandler;
import cn.zay.zayboot.util.UrlUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.AsciiString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuang.kou
 * @createTime 2020年09月23日 17:33:00
 **/
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final String FAVICON_ICO = "/favicon.ico";
    private static final AsciiString CONNECTION = AsciiString.cached("Connection");
    private static final AsciiString KEEP_ALIVE = AsciiString.cached("keep-alive");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) {
        log.info("要处理的Http请求:{}", fullHttpRequest);
        String uri = fullHttpRequest.uri();
        if (uri.equals(FAVICON_ICO)) {
            return;
        }
        //根据请求的类型获取对应的请求处理器
        RequestHandler requestHandler = RequestHandlerFactory.get(fullHttpRequest.method());
        FullHttpResponse fullHttpResponse;
        try {
            //核心!! 处理请求, 返回响应
            fullHttpResponse = requestHandler.handle(fullHttpRequest);
        } catch (Throwable e) {
            log.error("异常!!", e);
            //遇到异常, 返回错误响应体, 并返回500错误
            String requestPath = UrlUtil.getRequestPath(fullHttpRequest.uri());
            fullHttpResponse = FullHttpResponseFactory.getErrorResponse(requestPath, e.toString(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
        log.debug("要返回的响应体:{}",fullHttpResponse);
        boolean keepAlive = HttpUtil.isKeepAlive(fullHttpRequest);
        if (!keepAlive) {
            ctx.write(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
        } else {
            fullHttpResponse.headers().set(CONNECTION, KEEP_ALIVE);
            ctx.write(fullHttpResponse);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }


}
