package cn.zay.zayboot.mvc.handler;

import cn.zay.zayboot.core.ioc.BeanFactory;
import cn.zay.zayboot.exception.ResourceNotFoundException;
import cn.zay.zayboot.mvc.FullHttpResponseFactory;
import cn.zay.zayboot.mvc.MappingMethodDetail;
import cn.zay.zayboot.mvc.ParameterResolverFactory;
import cn.zay.zayboot.mvc.RouteMethodFactory;
import cn.zay.zayboot.mvc.resolver.ParameterResolver;
import cn.zay.zayboot.util.UrlUtil;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理 post请求的处理器
 * @author ZAY
 */
@Slf4j
public class PostRequestHandler implements RequestHandler {

    @Override
    public FullHttpResponse handle(FullHttpRequest fullHttpRequest) throws Exception{
        String requestUri = fullHttpRequest.uri();
        // get http request path，such as "/user"
        String requestPath = UrlUtil.getRequestPath(requestUri);
        return FullHttpResponseFactory.buildSuccessResponse();
    }
    private String getContentType(HttpHeaders headers) {
        String typeStr = headers.get("Content-Type");
        String[] list = typeStr.split(";");
        return list[0];
    }
}


