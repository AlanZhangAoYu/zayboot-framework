package cn.zay.zayboot.core.aop;

import cn.zay.zayboot.util.ReflectionUtil;
import java.lang.reflect.Method;

/**
 * @author ZAY
 * 方法执行, 用来执行原生方法或要切入的方法
 */
public class MethodInvocation {
    /**
     * 要被代理的对象
     */
    private final Object targetObject;
    /**
     * 要被代理的方法
     */
    private final Method targetMethod;
    /**
     * 目标方法的参数
     */
    private final Object[] args;
    public MethodInvocation(Object targetObject, Method targetMethod, Object[] args) {
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.args = args;
    }
    public Object proceed() {
        return ReflectionUtil.executeTargetMethod(targetObject, targetMethod, args);
    }

    public Method getTargetMethod() {
        return targetMethod;
    }
    public Object getTargetObject() {
        return targetObject;
    }
    public Object[] getArgs() {
        return args;
    }
}
