package cn.zay.zayboot.mvc;

import cn.zay.zayboot.core.ioc.BeanFactory;
import cn.zay.zayboot.mvc.resolver.ParameterResolver;
import cn.zay.zayboot.util.ObjectUtil;
import cn.zay.zayboot.util.ReflectionUtil;
import cn.zay.zayboot.util.UrlUtil;
import io.netty.handler.codec.http.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 对一个请求地址 uri与其对应的 controller方法的封装
 * @author ZAY
 */
@Data
public class MappingMethodDetail {
    /**
     * 请求对应的 controller方法
     */
    private Method method;
    /**
     * 请求的 url中包含的参数信息
     */
    private Map<String, Object> urlParameterMap;
    /**
     * 请求体中包含的参数信息
     */
    private Map<String, Object> queryParameterMap;
    public MappingMethodDetail(Method method, Map<String, Object> urlParameterMap, Map<String, Object> queryParameterMap) {
        this.method = method;
        this.urlParameterMap = urlParameterMap;
        this.queryParameterMap = queryParameterMap;
    }
    /**
     * 运行该 method的方法
     * @return
     */
    public Object run(){
        //获取当前方法所属的对象
        String beanName = BeanFactory.getBeanName(this.getMethod().getDeclaringClass());
        Object targetObject = BeanFactory.BEANS.get(beanName);
        //获取当前方法的参数, 保存在 methodParamList中
        List<Object> methodParamList = new ArrayList<>();
        if(!urlParameterMap.isEmpty()){
            for (String s : urlParameterMap.keySet()) {
                methodParamList.add(urlParameterMap.get(s));
            }
        }
        if(!queryParameterMap.isEmpty()){
            for (String s : queryParameterMap.keySet()) {
                methodParamList.add(queryParameterMap.get(s));
            }
        }
        if (this.method.getReturnType() == void.class) {
            //如果 targetMethod的返回类型为 void
            ReflectionUtil.executeTargetMethodNoResult(targetObject, this.method, methodParamList.toArray());
            return "-0xffffff";
        } else {
            //如果 targetMethod的返回类型不为 void (一般大多都会执行这个分支)
            return ReflectionUtil.executeTargetMethod(targetObject, method, methodParamList.toArray());
        }
    }
}

