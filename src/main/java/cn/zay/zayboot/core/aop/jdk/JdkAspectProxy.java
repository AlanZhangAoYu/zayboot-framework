package cn.zay.zayboot.core.aop.jdk;

import cn.zay.zayboot.core.aop.Interceptor;
import cn.zay.zayboot.core.aop.MethodInvocation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author ZAY
 * JDK动态代理
 */
public class JdkAspectProxy implements InvocationHandler {
    /**
     * 要代理的方法所在的对象 (该对象必须被 BeanFactory管理)
     */
    private final Object target;
    /**
     * 要修饰到 target上的拦截器, 包含要切入的方法和切点
     */
    private final Interceptor interceptor;
    private JdkAspectProxy(Object target, Interceptor interceptor) {
        this.target = target;
        this.interceptor = interceptor;
    }
    /**
     * 根据被代理对象 target生成代理对象
     * @param target 要被修饰(代理)的对象
     * @param interceptor 要修饰到 target上的拦截器
     * @return 返回修饰好的新的代理对象
     */
    public static Object wrap(Object target, Interceptor interceptor) {
        JdkAspectProxy jdkAspectProxy = new JdkAspectProxy(target, interceptor);
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), jdkAspectProxy);
    }
    /**
     * da
     * @param proxy
     * @param method
     * @param args
     * @return
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        MethodInvocation methodInvocation = new MethodInvocation(target, method, args);
        //返回值仍然是代理类执行的结果
        return interceptor.agent(methodInvocation);
    }
}
