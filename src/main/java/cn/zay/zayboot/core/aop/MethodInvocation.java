package cn.zay.zayboot.core.aop;

import cn.zay.zayboot.util.ReflectionUtil;
import java.lang.reflect.Method;

/**
 * @author ZAY
 * 方法调用(执行)器, 用来执行原生方法或要切入的方法
 */
public class MethodInvocation {
    /**
     * 被执行方法所在的对象
     */
    private final Object targetObject;
    /**
     * 要被执行的方法
     */
    private final Method targetMethod;
    /**
     * 要被执行的方法的参数
     */
    private final Object[] args;
    public MethodInvocation(Object targetObject, Method targetMethod, Object[] args) {
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.args = args;
    }
    /**
     * 执行有参目标方法
     * @return 目标方法执行的结果
     */
    public Object run(Object[] args) {
        return ReflectionUtil.executeTargetMethod(targetObject, targetMethod, args);
    }
    /**
     * 执行无参目标方法
     * @return 目标方法执行的结果
     */
    public Object run() {
        return ReflectionUtil.executeTargetMethod(targetObject, targetMethod);
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
