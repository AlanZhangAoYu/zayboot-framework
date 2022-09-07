package cn.zay.zayboot.core.aop;

import cn.zay.zayboot.annotation.aop.Aspect;
import cn.zay.zayboot.core.ioc.BeanFactory;
import cn.zay.zayboot.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author ZAY
 * 存放所有拦截器的拦截器工厂类
 */
@Slf4j
public class InterceptorFactory {
    /**
     * 存放生成的拦截器(代理后的方法)的 Map (容器)
     */
    public static final Map<String,Interceptor> interceptors = new HashMap<>();
    /**
     * 存放扫描到的所有被 @Pointcut注释的方法, 格式为: key: "xxx.xxx.xxx.aaaBbb()" -> value: 被注释的方法的方法执行器
     */
    public static final Map<String, MethodInvocation> pointcutMethodsMap = new HashMap<>();
    /**
     * 存放扫描到的所有被 @Before注释的方法, 格式为: key: "xxx.xxx.xxx.aaaBbb()" -> value: 被注释的方法的方法执行器
     */
    public static final Map<String, MethodInvocation> beforeMethodsMap = new HashMap<>();
    /**
     * 存放扫描到的所有被 @After注释的方法, 格式为: key: "xxx.xxx.xxx.aaaBbb()" -> value: 被注释的方法的方法执行器
     */
    public static final Map<String, MethodInvocation> afterMethodsMap = new HashMap<>();
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
            interceptors.put(interceptorClass.getName(),(Interceptor) ReflectionUtil.newInstance(interceptorClass));
        }
        // 遍历所有被 @Aspect标记的类
        aspects.forEach(aClass -> {
            //创建 Aspect对象
            Object obj = ReflectionUtil.newInstance(aClass);
            //扫描 Aspect对象中的 @Pointcut、@Before以及 @After, 整合切入, 生成拦截器
            Interceptor interceptor = new InternallyAspectInterceptor(obj);
            //向列表中添加生成好的拦截器
            interceptors.add(interceptor);
        });
        // 添加 Bean验证拦截器
        interceptors.add(new BeanValidationInterceptor());
    }
}
