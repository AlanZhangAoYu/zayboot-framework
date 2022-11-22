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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

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
        // get target method
        MappingMethodDetail methodDetail = RouteMethodFactory.getMethodDetail(requestPath, HttpMethod.POST);
        Method targetMethod = methodDetail.getMethod();
        if (targetMethod == null) {
            throw new ResourceNotFoundException("请求的资源["+requestUri+"]不存在");
        }
        String contentType = this.getContentType(fullHttpRequest.headers());
        // target method parameters.
        // notice! you should convert it to array when pass into the executeMethod()
        List<Object> targetMethodParams = new ArrayList<>();
        if ("application/json".equals(contentType)) {
            String json = fullHttpRequest.content().toString(Charsets.toCharset(CharEncoding.UTF_8));
            methodDetail.setJson(json);
            Parameter[] targetMethodParameters = targetMethod.getParameters();
            for (Parameter parameter : targetMethodParameters) {
                ParameterResolver parameterResolver = ParameterResolverFactory.get(parameter);
                if (parameterResolver != null) {
                    Object param = parameterResolver.resolve(methodDetail, parameter);
                    targetMethodParams.add(param);
                }
            }
        } else {
            throw new IllegalArgumentException("only receive application/json type data");
        }
        String beanName = BeanFactory.getBeanName(methodDetail.getMethod().getDeclaringClass());
        Object targetObject = BeanFactory.BEANS.get(beanName);
        return FullHttpResponseFactory.getSuccessResponse(targetMethod, targetMethodParams, targetObject);
    }

    private String getContentType(HttpHeaders headers) {
        String typeStr = headers.get("Content-Type");
        String[] list = typeStr.split(";");
        return list[0];
    }
}


