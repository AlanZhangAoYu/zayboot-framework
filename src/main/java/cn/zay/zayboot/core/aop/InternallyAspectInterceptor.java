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
import java.util.Objects;

/**
 * @author ZAY
 */
public class InternallyAspectInterceptor extends Interceptor{
    private final Object adviceBean;
    private final HashSet<String> expressionUrls = new HashSet<>();
    private final List<Method> beforeMethods = new ArrayList<>();
    private final List<Method> afterMethods = new ArrayList<>();
    public InternallyAspectInterceptor(Object adviceBean) {
        this.adviceBean = adviceBean;
        init();
    }
    private void init() {
        for (Method method : adviceBean.getClass().getMethods()) {
            Pointcut pointcut = method.getAnnotation(Pointcut.class);
            if (!Objects.isNull(pointcut)) {
                expressionUrls.add(pointcut.value());
            }
            Before before = method.getAnnotation(Before.class);
            if (!Objects.isNull(before)) {
                beforeMethods.add(method);
            }
            After after = method.getAnnotation(After.class);
            if (!Objects.isNull(after)) {
                afterMethods.add(method);
            }
        }
    }
    @Override
    public boolean supports(Object bean) {
        return expressionUrls.stream().anyMatch(url -> StringUtil.simpleMatch(url, bean.getClass().getName())) && (!beforeMethods.isEmpty() || !afterMethods.isEmpty());
    }
    @Override
    public Object intercept(MethodInvocation methodInvocation) {
        JoinPoint joinPoint = new JoinPointImpl(adviceBean, methodInvocation.getTargetObject(),
                methodInvocation.getArgs());
        beforeMethods.forEach(method -> ReflectionUtil.executeTargetMethodNoResult(adviceBean, method, joinPoint));
        Object result = methodInvocation.proceed();
        afterMethods.forEach(method -> ReflectionUtil.executeTargetMethodNoResult(adviceBean, method, result, joinPoint));
        return result;
    }
}
