package cn.zay.zayboot.core.aop;

import cn.zay.zayboot.annotation.aop.After;
import cn.zay.zayboot.annotation.aop.Before;
import cn.zay.zayboot.annotation.aop.Pointcut;
import cn.zay.zayboot.util.ReflectionUtil;
import cn.zay.zayboot.util.StringUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
/**
 * @author ZAY
 * 拦截器的实现类
 */
public class InternallyAspectInterceptor extends Interceptor{
    /**
     * 被代理方法所在的类
     */
    private final Object adviceBean;
    /**
     * 用来存放所有 @Pointcut注释中的值
     */
    private final HashSet<String> expressionUrls = new HashSet<>();
    /**
     * 用来存放所有要切入被代理方法之前的方法
     */
    private final List<Method> beforeMethods = new ArrayList<>();
    /**
     * 用来存放所有要切入被代理方法之后的方法
     */
    private final List<Method> afterMethods = new ArrayList<>();
    public InternallyAspectInterceptor(Object adviceBean) {
        this.adviceBean = adviceBean;
        init();
    }
    /**
     * 扫描 adviceBean中所有的方法, 寻找切点、要切入的方法并将其存入对应的集合以备后续使用
     */
    private void init() {
        for (Method method : adviceBean.getClass().getMethods()) {
            Pointcut pointcut = method.getAnnotation(Pointcut.class);
            if (pointcut != null) {
                expressionUrls.add(pointcut.value());
            }
            Before before = method.getAnnotation(Before.class);
            if (before != null) {
                beforeMethods.add(method);
            }
            After after = method.getAnnotation(After.class);
            if (after != null) {
                afterMethods.add(method);
            }
        }
    }
    @Override
    public boolean supports(Object bean) {
        return expressionUrls.stream().anyMatch(url -> StringUtil.simpleMatch(url, bean.getClass().getName())) && (!beforeMethods.isEmpty() || !afterMethods.isEmpty());
    }
    /**
     * 在方法执行器 methodInvocation前后执行 beforeMethods和 afterMethods中的方法, 完成 aop代理
     * @param methodInvocation 被代理的方法的执行器
     * @return 返回原本被代理的方法的方法执行器的执行结果
     */
    @Override
    public Object agent(MethodInvocation methodInvocation) {
        JoinPoint joinPoint = new JoinPointImpl(adviceBean, methodInvocation.getTargetObject(),
                methodInvocation.getArgs());
        beforeMethods.forEach(method -> ReflectionUtil.executeTargetMethodNoResult(adviceBean, method, joinPoint));
        Object result = methodInvocation.run();
        afterMethods.forEach(method -> ReflectionUtil.executeTargetMethodNoResult(adviceBean, method, result, joinPoint));
        return result;
    }
}
