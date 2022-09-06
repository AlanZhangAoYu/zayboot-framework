package cn.zay.zayboot.core.aop;

import cn.zay.zayboot.core.aop.cglib.CglibAopProxyBeanPostProcessor;
import cn.zay.zayboot.core.aop.jdk.JdkAopProxyBeanPostProcessor;

/**
 * @author ZAY
 * Aop代理 Bean后处理器工厂
 */
public class AopProxyBeanPostProcessorFactory {
    /**
     * @param beanClass 目标类
     * @return beanClass实现了接口就返回 jdk动态代理 bean后置处理器,否则返回 cglib动态代理 bean后置处理器
     */
    public static BeanPostProcessor get(Class<?> beanClass) {
        if (beanClass.isInterface() || beanClass.getInterfaces().length > 0) {
            return new JdkAopProxyBeanPostProcessor();
        } else {
            return new CglibAopProxyBeanPostProcessor();
        }
    }
}
