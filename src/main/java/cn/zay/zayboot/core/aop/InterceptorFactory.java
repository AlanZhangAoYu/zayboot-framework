package cn.zay.zayboot.core.aop;

import cn.zay.zayboot.annotation.aop.Aspect;
import cn.zay.zayboot.annotation.aop.Order;
import cn.zay.zayboot.core.ioc.BeanFactory;
import cn.zay.zayboot.exception.CannotInitializeConstructorException;
import cn.zay.zayboot.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ZAY
 * 存放所有拦截器的拦截器工厂类
 */
@Slf4j
public class InterceptorFactory {
    /**
     * 存放所有实现了 Interceptor接口的类(所有的拦截器)的列表 (容器)
     */
    private static List<Interceptor> interceptors = new ArrayList<>();
    /**
     * 加载指定包路径下被 BeanFactory代理的类中所有的拦截器 Interceptor以及所有被 @Aspect标记的类
     * @param packageName 指定的包路径, 一般为 @ComponentScan中设置的值
     */
    public static void loadInterceptors(String[] packageName){
        // 获取指定包中实现了 Interceptor 接口的类
        Set<Class<? extends Interceptor>> interceptorClasses = ReflectionUtil.getSubClass(packageName, Interceptor.class);
        // 获取被 @Aspect标记的类
        Set<Class<?>> aspects = BeanFactory.CLASSES.get(Aspect.class);
        // 遍历所有拦截器类, 向 interceptors中添加创建好的拦截器对象
        for (Class<? extends Interceptor> interceptorClass : interceptorClasses) {
            interceptors.add((Interceptor) ReflectionUtil.newInstance(interceptorClass));
        }
        // 遍历所有被 @Aspect标记的类
        aspects.forEach(aClass -> {
            //创建 Aspect对象
            Object obj = ReflectionUtil.newInstance(aClass);
            //扫描 Aspect对象中的 @Pointcut、@Before以及 @After, 整合切入, 生成拦截器
            Interceptor interceptor = new InternallyAspectInterceptor(obj);
            //若 Aspect对象被 @Order注释, 就设置拦截器的优先级 order为注释的值
            if (aClass.isAnnotationPresent(Order.class)) {
                Order order = aClass.getAnnotation(Order.class);
                interceptor.setOrder(order.value());
            }
            //向列表中添加生成好的拦截器
            interceptors.add(interceptor);
        });
        // 添加 Bean验证拦截器
        interceptors.add(new BeanValidationInterceptor());
        // 根据 order为拦截器排序
        interceptors = interceptors.stream().sorted(Comparator.comparing(Interceptor::getOrder)).collect(Collectors.toList());
    }

    public static List<Interceptor> getInterceptors() {
        return interceptors;
    }
}
