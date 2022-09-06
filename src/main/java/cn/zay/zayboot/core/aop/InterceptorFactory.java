package cn.zay.zayboot.core.aop;

import cn.zay.zayboot.annotation.aop.Aspect;
import cn.zay.zayboot.annotation.aop.Order;
import cn.zay.zayboot.core.ioc.BeanFactory;
import cn.zay.zayboot.exception.CannotInitializeConstructorException;
import cn.zay.zayboot.util.ReflectionUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ZAY
 * 存放所有拦截器的拦截器工厂类
 */
public class InterceptorFactory {
    private static List<Interceptor> interceptors = new ArrayList<>();
    /**
     * 加载指定包路径下被 BeanFactory代理的类中所有的拦截器 Interceptor以及所有被 @Aspect标记的类
     * @param packageName 指定的包路径, 一般为 @ComponentScan中设置的值
     * @throws Exception 有些类实现了 Interceptor接口, 但是是抽象类, 或者没有空的构造函数, 可能抛出异常
     */
    public static void loadInterceptors(String[] packageName) throws Exception{
        // 获取指定包中实现了 Interceptor 接口的类
        Set<Class<? extends Interceptor>> interceptorClasses = ReflectionUtil.getSubClass(packageName, Interceptor.class);
        // 获取被 @Aspect 标记的类
        Set<Class<?>> aspects = BeanFactory.CLASSES.get(Aspect.class);
        // 遍历所有拦截器类
        for (Class<? extends Interceptor> interceptorClass : interceptorClasses) {
            try {
                interceptors.add(interceptorClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new CannotInitializeConstructorException("不能初始化构造函数,该接口的名称是:" + interceptorClass.getSimpleName());
            }
        }
        aspects.forEach(aClass -> {
            Object obj = ReflectionUtil.newInstance(aClass);
            Interceptor interceptor = new InternallyAspectInterceptor(obj);
            if (aClass.isAnnotationPresent(Order.class)) {
                Order order = aClass.getAnnotation(Order.class);
                interceptor.setOrder(order.value());
            }
            interceptors.add(interceptor);
        });
        // 添加Bean验证拦截器
        interceptors.add(new BeanValidationInterceptor());
        // 根据 order为拦截器排序
        interceptors = interceptors.stream().sorted(Comparator.comparing(Interceptor::getOrder)).collect(Collectors.toList());
    }

    public static List<Interceptor> getInterceptors() {
        return interceptors;
    }
}
