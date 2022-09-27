package cn.zay.zayboot.mvc;

import cn.zay.zayboot.util.JsonUtil;
import cn.zay.zayboot.util.ReflectionUtil;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.AsciiString;
import java.lang.reflect.Method;
import java.util.List;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author ZAY
 */
public class FullHttpResponseFactory {
    private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
    private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");
    public static FullHttpResponse getSuccessResponse(Method targetMethod, List<Object> targetMethodParams, Object targetObject) {
        if (targetMethod.getReturnType() == void.class) {
            //如果 targetMethod的返回类型为 void
            ReflectionUtil.executeTargetMethodNoResult(targetObject, targetMethod, targetMethodParams.toArray());
            return buildSuccessResponse();
        } else {
            //如果 targetMethod的返回类型不为 void, 一般大多都会执行这个分支
            Object result = ReflectionUtil.executeTargetMethod(targetObject, targetMethod, targetMethodParams.toArray());
            return buildSuccessResponse(result);
        }
    }
    public static FullHttpResponse getErrorResponse(String url, String message, HttpResponseStatus httpResponseStatus) {
        ErrorResponse errorResponse = new ErrorResponse(httpResponseStatus.code(), httpResponseStatus.reasonPhrase(), message, url);
        byte[] content = JsonUtil.serialize(errorResponse);
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, httpResponseStatus, Unpooled.wrappedBuffer(content));
        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }
    private static FullHttpResponse buildSuccessResponse(Object o) {
        byte[] content = JsonUtil.serialize(o);
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content));
        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }
    private static FullHttpResponse buildSuccessResponse() {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }
}
