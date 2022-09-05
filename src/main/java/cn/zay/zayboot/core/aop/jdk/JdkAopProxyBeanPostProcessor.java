package cn.zay.zayboot.core.aop.jdk;

import cn.zay.zayboot.core.aop.AbstractAopProxyBeanPostProcessor;
import cn.zay.zayboot.core.aop.Interceptor;

public class JdkAopProxyBeanPostProcessor extends AbstractAopProxyBeanPostProcessor {
    @Override
    public Object wrapBean(Object target, Interceptor interceptor) {
        return JdkAspectProxy.wrap(target,interceptor);
    }
}
