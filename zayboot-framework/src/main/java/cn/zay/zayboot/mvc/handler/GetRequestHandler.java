package cn.zay.zayboot.mvc.handler;

import cn.zay.zayboot.exception.ResourceNotFoundException;
import cn.zay.zayboot.mvc.FullHttpResponseFactory;
import cn.zay.zayboot.mvc.MappingMethodDetail;
import cn.zay.zayboot.mvc.RouteMethodFactory;
import cn.zay.zayboot.util.UrlUtil;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 处理 get请求的处理器
 * @author ZAY
 */
@Slf4j
public class GetRequestHandler implements RequestHandler {
    @Override
    public FullHttpResponse handle(FullHttpRequest fullHttpRequest) throws Exception{
        /*
          此时接收到的 requestUri为: /hello?recipient=world&x=1;y=2
          或者为: /hello/132456/getName/zhangaoyu
        */
        String requestUri = fullHttpRequest.uri();
        /*
          获取当前请求的 uri
          例如由 /hello?recipient=world&x=1;y=2得到 /hello
          这里如何通过 /hello/132456/getName/zhangaoyu得到 /hello/{id}/getName/{name}
          是个大问题
          解决方法: 使用UrlUtil.formatUrl(uri)将 uri转换为正则表达式, 做为 REQUEST_METHOD_MAP中的 key保存,当
          请求进来时做匹配
        */
        //在 REQUEST_METHOD_MAP中获取到对应的 Controller方法
        Method targetMethod = getMethodByUri(requestUri);
        if (targetMethod == null) {
            throw new ResourceNotFoundException("请求的资源["+requestUri+"]不存在, 请检查url或请求类型是否错误");
        }
        log.debug("请求uri:{} -> 对应方法:{}",UrlUtil.getRequestPath(requestUri), targetMethod.getName());
        //获取请求 url中携带的参数
        Map<String,Object> uriParam = getUrlParam(requestUri);
        //获取请求体中携带的参数
        //get请求可不可以携带请求体???????参考: https://zhuanlan.zhihu.com/p/456921996
        Map<String,Object> bodyParam = UrlUtil.getPostRequestParams(fullHttpRequest);
        //生成 MappingMethodDetail对象并执行后台方法
        MappingMethodDetail methodDetail = new MappingMethodDetail(targetMethod,uriParam,bodyParam);
        Object runResult = methodDetail.run();
        //这里可能有问题
        if("-0xffffff".equals(runResult)){
            return FullHttpResponseFactory.buildSuccessResponse();
        }
        return FullHttpResponseFactory.buildSuccessResponse(runResult);
    }

    /**
     * 通过 uri在 REQUEST_METHOD_MAP中获取到对应的 Controller方法
     * @param uri 格式为: /hello?recipient=world&x=1;y=2 或 /hello/132456/getName/zhangaoyu 的 uri
     * @return 在 REQUEST_METHOD_MAP中找到的方法, 找不到返回 null
     */
    private Method getMethodByUri(String uri){
        if(uri.contains("?") || uri.contains("&")){
            //如果 url中存在 '?'或 '&', 就说明是常规格式
            String requestPath = UrlUtil.getRequestPath(uri);
            return RouteMethodFactory.getRequestMethodMap().get(HttpMethod.GET).get(UrlUtil.formatUrl(requestPath));
        } else {
            //如果不存在 '?'或 '&', 则是符合 RestFul规范的 api
            //查出所有的方法名(以正则表达式方式存储)
            Set<String> methodNameSet = RouteMethodFactory.getRequestMethodMap().get(HttpMethod.GET).keySet();
            for (String s : methodNameSet) {
                if(Pattern.matches(s,uri)){
                    return RouteMethodFactory.getRequestMethodMap().get(HttpMethod.GET).get(s);
                }
            }
        }
        return null;
    }

    /**
     * 获取当前请求 uri中携带的参数
     * @return
     */
    private Map<String,Object> getUrlParam(String uri){
        Map<String,Object> uriParam = null;
        if(uri.contains("?") || uri.contains("&")){
            //如果 url中存在 '?'或 '&', 就说明是常规格式
            uriParam = UrlUtil.getUrlParameterMapForCommon(uri);
        } else {
            //如果不存在 '?'或 '&', 则是符合 RestFul规范的 api
            //查出所有的方法名(以正则表达式方式存储)
            Set<String> methodNameSet = RouteMethodFactory.getRequestMethodMap().get(HttpMethod.GET).keySet();
            for (String s : methodNameSet) {
                if(Pattern.matches(s,uri)){
                    uriParam = UrlUtil.getUrlParameterMapForRestFul(uri,
                            RouteMethodFactory.getRequestUrlMap().get(HttpMethod.GET).get(s));
                }
            }
        }
        return uriParam;
    }
}
