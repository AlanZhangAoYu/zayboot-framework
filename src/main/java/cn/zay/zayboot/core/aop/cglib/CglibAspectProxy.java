package cn.zay.zayboot.core.aop.cglib;

import cn.zay.zayboot.core.aop.Interceptor;
import cn.zay.zayboot.core.aop.InterceptorFactory;
import cn.zay.zayboot.exception.UnrecognizedPointcutMethodException;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;

/**
 * @author ZAY
 * Cglib动态代理
 */
public class CglibAspectProxy implements MethodInterceptor {
    private final String className;
    public CglibAspectProxy(String className){
        this.className = className;
    }
    /**
     *
     * @param obj 代理对象 (增强后的对象)
     * @param method 被拦截的方法 (需要增强的方法)
     * @param args 被拦截的方法的入参
     * @param proxy 用于调用原始方法
     * @return 被增强后方法执行的结果
     * @throws Throwable 若没有找到对应方法抛出异常
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable{
        //获取代理类中方法的全名, 例如: xxx.xxx.Xxx.aaaBbb
        String methodAllName = className + "." + method.getName();
        /*
        String methodAllName = obj.getClass().getName()+"."+method.getName();
        若使用以上代码, 获取到的 methodAllName为: cn.zay.demo.java.pojo.People$$EnhancerByCGLIB$$262aeff7.working
        有一堆不需要的东西在干扰查询, 这里体现出 cglib代理类的原理为生成其子类
        */
        Interceptor interceptor = InterceptorFactory.INTERCEPTORS.get(methodAllName);
        if(interceptor == null){
            throw new UnrecognizedPointcutMethodException("未能识别该["+methodAllName+"]Pointcut方法, 请确任该方法存在且被@Pointcut注释");
        }
        return interceptor.agent(args);
    }
}
