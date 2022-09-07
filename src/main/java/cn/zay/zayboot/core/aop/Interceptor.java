package cn.zay.zayboot.core.aop;

/**
 * @author ZAY
 * 拦截器
 */
public abstract class Interceptor {
    public boolean supports(Object bean) {
        return false;
    }
    /**
     * 对 methodInvocation方法实现拦截
     * @param methodInvocation 要拦截的方法
     * @return 返回拦截后方法执行的结果
     */
    public abstract Object agent(MethodInvocation methodInvocation);
}
