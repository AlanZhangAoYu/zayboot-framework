package cn.zay.zayboot.core.aop;

import cn.zay.zayboot.annotation.aop.After;
import cn.zay.zayboot.annotation.aop.Aspect;
import cn.zay.zayboot.annotation.aop.Before;
import cn.zay.zayboot.annotation.aop.Pointcut;
import cn.zay.zayboot.core.ioc.BeanFactory;
import cn.zay.zayboot.exception.UnrecognizedPointcutMethodException;
import cn.zay.zayboot.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
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
    public static final Map<String,Interceptor> INTERCEPTORS = new HashMap<>();
    /**
     * 存放扫描到的所有被 @Pointcut注释的方法, 格式为: key: "xxx.xxx.Xxx.aaaBbb" -> value: 被注释的方法的方法执行器
     */
    public static final Map<String, MethodInvocation> POINTCUT_METHODS_MAP = new HashMap<>();
    /**
     * 存放扫描到的所有被 @Before注释的方法, 格式为: key: "xxx.xxx.Xxx.aaaBbb" -> value: 被注释的方法的方法执行器
     * 注: 被注释的方法只能返回 void
     */
    public static final Map<String, JoinPoint> BEFORE_METHODS_MAP = new HashMap<>();
    /**
     * 存放扫描到的所有被 @After注释的方法, 格式为: key: "xxx.xxx.Xxx.aaaBbb" -> value: 被注释的方法的方法执行器
     * 注: 被注释的方法只能返回 void
     */
    public static final Map<String, JoinPoint> AFTER_METHODS_MAP = new HashMap<>();
    /**
     * 加载指定包路径下被 BeanFactory代理的类中所有的拦截器 Interceptor以及所有被 @Aspect标记的类
     * @param packageName 指定的包路径, 一般为 @ComponentScan中设置的值
     */
    public static void loadInterceptors(String[] packageName) throws Exception{
        //获取指定包中实现了 Interceptor 接口的类
        Set<Class<? extends Interceptor>> interceptorClasses = ReflectionUtil.getSubClass(packageName, Interceptor.class);
        //获取被 @Aspect标记的类
        Set<Class<?>> aspects = BeanFactory.CLASSES.get(Aspect.class);
        //遍历所有拦截器类, 向 interceptors中添加创建好的拦截器对象
        for (Class<? extends Interceptor> interceptorClass : interceptorClasses) {
            INTERCEPTORS.put(interceptorClass.getName(),(Interceptor) ReflectionUtil.newInstance(interceptorClass));
        }
        //遍历所有被 @Aspect标记的类
        aspects.forEach(aClass -> {
            //创建 Aspect对象
            Object obj = ReflectionUtil.newInstance(aClass);
            //找出 Aspect对象中的所有方法
            Method[] methods = aClass.getMethods();
            //向那三个 Map中添加对应的 Pointcut、Before、After方法
            for (Method method : methods) {
                if(method.isAnnotationPresent(Pointcut.class)){
                    POINTCUT_METHODS_MAP.put(aClass.getName()+"."+method.getName(), new MethodInvocation(obj,method));
                }
                if(method.isAnnotationPresent(Before.class)){
                    Before before = method.getAnnotation(Before.class);
                    String[] beforeValue = before.value();
                    JoinPoint joinPoint = new JoinPoint(new MethodInvocation(obj,method),beforeValue);
                    BEFORE_METHODS_MAP.put(aClass.getName()+"."+method.getName(), joinPoint);
                }
                if(method.isAnnotationPresent(After.class)){
                    After after = method.getAnnotation(After.class);
                    String[] afterValue = after.value();
                    JoinPoint joinPoint = new JoinPoint(new MethodInvocation(obj,method),afterValue);
                    AFTER_METHODS_MAP.put(aClass.getName()+"."+method.getName(), joinPoint);
                }
            }
        });
        //给每一个 POINTCUT_METHODS_MAP中的方法生成拦截器
        for (String pointCutMethodName : POINTCUT_METHODS_MAP.keySet()) {
            Interceptor interceptor =
                    new InternallyAspectInterceptor(POINTCUT_METHODS_MAP.get(pointCutMethodName));
            //向 INTERCEPTORS中添加生成好的拦截器
            INTERCEPTORS.put(pointCutMethodName,interceptor);
        }
        // 添加 Bean验证拦截器
        INTERCEPTORS.put("beanValidationInterceptor",new BeanValidationInterceptor());
    }
    /**
     * 运行最终生成的代理方法(原被代理方法返回值为 void)
     * @param methodName 方法名, 格式为: "xxx.xxx.xx.Xxx.aaaBbb"
     * @param args 方法的参数名
     * @throws Exception 方法不存在或未被注册为 Pointcut, 会抛出异常
     */
    public static void runProxyMethodWithReturnValueOfVoid(String methodName, Object... args)throws Exception{
        if(POINTCUT_METHODS_MAP.get(methodName) != null){
            INTERCEPTORS.get(methodName).agent(args);
        }else{
            throw new UnrecognizedPointcutMethodException("未能识别该["+methodName+"]Pointcut方法, 请确任该方法存在且被@Pointcut注释");
        }
    }
    /**
     * 运行最终生成的代理方法(原被代理方法返回值不为 void)
     * @param methodName 方法名, 格式为: "xxx.xxx.xx.Xxx.aaaBbb"
     * @param args 方法的参数名
     * @return 返回原被代理方法运行的返回值
     * @throws Exception 方法不存在或未被注册为 Pointcut, 会抛出异常
     */
    public static Object runProxyMethodWhoseReturnValueIsNotVoid(String methodName, Object... args)throws Exception{
        if(POINTCUT_METHODS_MAP.get(methodName) != null){
            return INTERCEPTORS.get(methodName).agent(args);
        }else{
            throw new UnrecognizedPointcutMethodException("未能识别该["+methodName+"]Pointcut方法, 请确任该方法存在且被@Pointcut注释");
        }
    }
}
