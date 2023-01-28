package cn.zay.zayboot.mvc;

import cn.zay.zayboot.core.ioc.BeanFactory;
import cn.zay.zayboot.mvc.resolver.ParameterResolver;
import cn.zay.zayboot.util.ReflectionUtil;
import lombok.Data;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

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
     * <p>解析 @RequestBody, @RequestParam, @PathVariable等注解</p>
     * <p>注入参数</p>
     * <p>运行该 method的方法</p>
     * @return 方法执行后的返回值, 如果没有返回值, 返回 -0xffffff, 这么设计不知道有没有问题......
     */
    public Object run(){
        //获取当前方法所属的对象
        String beanName = BeanFactory.getBeanName(this.getMethod().getDeclaringClass());
        Object targetObject = BeanFactory.BEANS.get(beanName);
        //获取整合后的 map
        Map<String,Object> paramMap = handleParamMap(urlParameterMap,queryParameterMap);
        //获取当前方法要执行所需要的参数, 保存在 methodParamList中
        List<Object> methodParamList = new ArrayList<>();
        for (Parameter parameter : this.method.getParameters()) {
            ParameterResolver parameterResolver = ParameterResolverFactory.get(parameter);
            if(parameterResolver == null){
                throw new IllegalArgumentException("该参数["+parameter+"]未指定注入参数!!");
            }
            if(!paramMap.isEmpty()){
                Object resolve = parameterResolver.resolve(parameter, paramMap);
                if(resolve == null){
                    throw new IllegalArgumentException("该参数["+parameter+"]注入出错!!");
                }
                methodParamList.add(resolve);
            }
        }
        if (this.method.getReturnType() == void.class) {
            //如果 targetMethod的返回类型为 void (一般都不会执行这个分支)
            ReflectionUtil.executeTargetMethodNoResult(targetObject, this.method, methodParamList.toArray());
            return "-0xffffff";
        } else {
            //如果 targetMethod的返回类型不为 void (一般大多都会执行这个分支)
            return ReflectionUtil.executeTargetMethod(targetObject, this.method, methodParamList.toArray());
        }
    }

    /**
     * <p>将 urlParameterMap与 queryParameterMap中的参数处理为一个 map,不能保证用户传过来些啥,处理后的参数尽量保证可以被执行</p>
     * <p>比如如果两个 map中存在重复的参数, 抛异常提醒用户</p>
     * <p>还有啥没想到的?? 再说</p>
     */
    private Map<String,Object> handleParamMap(Map<String, Object> urlParameterMap,Map<String, Object> queryParameterMap){
        for (String s : urlParameterMap.keySet()) {
            if(queryParameterMap.containsKey(s)){
                throw new IllegalArgumentException("url与请求体中存在重复参数, 请排查!");
            }
        }
        Map<String, Object> paramMap = new HashMap<>(16);
        paramMap.putAll(urlParameterMap);
        paramMap.putAll(queryParameterMap);
        return paramMap;
    }
}

