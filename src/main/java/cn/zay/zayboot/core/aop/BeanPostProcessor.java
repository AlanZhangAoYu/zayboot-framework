package cn.zay.zayboot.core.aop;

/**
 * @author ZAY
 * bean后置处理器接口
 */
public interface BeanPostProcessor {
    /**
     * 初始化后的后处理
     * @param bean 要代理的方法所在的类 (该类必须被 BeanFactory管理)
     * @return 返回被代理后的 bean
     */
    default Object postProcessAfterInitialization(Object bean) {
        return bean;
    }
}
