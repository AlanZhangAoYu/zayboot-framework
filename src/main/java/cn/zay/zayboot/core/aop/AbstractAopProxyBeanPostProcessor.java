package cn.zay.zayboot.core.aop;

/**
 * @author ZAY
 * 抽象的 Aop代理 Bean后置处理器
 */
public abstract class AbstractAopProxyBeanPostProcessor implements BeanPostProcessor{
    @Override
    public Object postProcessAfterInitialization(Object bean) {
        Object wrapperProxyBean = bean;
        //链式包装目标类
        /*for (Interceptor interceptor : InterceptorFactory.interceptors.entrySet()) {
            if (interceptor.supports(bean)) {
                wrapperProxyBean = wrapBean(wrapperProxyBean, interceptor);
            }
        }*/
        return wrapperProxyBean;
    }
    public abstract Object wrapBean(Object target, Interceptor interceptor);
}
