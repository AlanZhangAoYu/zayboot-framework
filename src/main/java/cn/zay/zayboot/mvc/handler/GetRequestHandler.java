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
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
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
 * 处理 get请求的处理器
 * @author ZAY
 */
@Slf4j
public class GetRequestHandler implements RequestHandler {
    @Override
    public FullHttpResponse handle(FullHttpRequest fullHttpRequest) throws Exception{
        String requestUri = fullHttpRequest.uri();
        Map<String, String> queryParameterMappings = getQueryParams(requestUri);
        // get http request path，such as "/user"
        String requestPath = UrlUtil.getRequestPath(requestUri);
        // get target method
        MappingMethodDetail methodDetail = RouteMethodFactory.getMethodDetail(requestPath, HttpMethod.GET);
        methodDetail.setQueryParameterMappings(queryParameterMappings);
        Method targetMethod = methodDetail.getMethod();
        if (targetMethod == null) {
            throw new ResourceNotFoundException("请求的资源["+requestUri+"]不存在");
        }
        log.debug("requestPath -> target method [{}]", targetMethod.getName());
        Parameter[] targetMethodParameters = targetMethod.getParameters();
        // target method parameters.
        // notice! you should convert it to array when pass into the executeMethod method
        List<Object> targetMethodParams = new ArrayList<>();
        for (Parameter parameter : targetMethodParameters) {
            ParameterResolver parameterResolver = ParameterResolverFactory.get(parameter);
            if (parameterResolver != null) {
                Object param = parameterResolver.resolve(methodDetail, parameter);
                targetMethodParams.add(param);
            }
        }
        String beanName = BeanFactory.getBeanName(methodDetail.getMethod().getDeclaringClass());
        Object targetObject = BeanFactory.BEANS.get(beanName);
        return FullHttpResponseFactory.getSuccessResponse(targetMethod, targetMethodParams, targetObject);
    }

    /**
     * 从 uri中获取参数
     * @param uri 要获取参数的 uri, 类似于: /user/123
     * @return 参数名与参数值的映射
     */
    private Map<String, String> getQueryParams(String uri) {
        //返回 URI的已解码键值参数对
        Map<String, List<String>> parameters = UrlUtil.getRequestParameters(uri);
        Map<String, String> resultMap = new HashMap<>(6);
        for (Map.Entry<String, List<String>> attr : parameters.entrySet()) {
            for (String attrVal : attr.getValue()) {
                resultMap.put(attr.getKey(), attrVal);
            }
        }
        return resultMap;
    }
}
