package cn.zay.zayboot.core.aop.cglib;

import net.sf.cglib.proxy.Enhancer;

/**
 * @author ZAY
 */
public class CglibProxyFactory {
    public static Object getProxy(Class<?> clazz) {
        Class<?> proxySuperClass = clazz;
        // cglib 多级代理处理
        if (clazz.getName().contains("$$")) {
            proxySuperClass = clazz.getSuperclass();
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(clazz.getClassLoader());
        enhancer.setSuperclass(proxySuperClass);
        enhancer.setCallback(new CglibAspectProxy(clazz.getName()));
        return enhancer.create();
    }
}
