package cn.zay.zayboot.core.aop.cglib;

import cn.zay.zayboot.core.aop.AbstractAopProxyBeanPostProcessor;
import cn.zay.zayboot.core.aop.Interceptor;

/**
 * @author ZAY
 */
public class CglibAopProxyBeanPostProcessor extends AbstractAopProxyBeanPostProcessor {
    @Override
    public Object wrapBean(Object target, Interceptor interceptor) {
        return CglibAspectProxy.wrap(target,interceptor);
    }
}
