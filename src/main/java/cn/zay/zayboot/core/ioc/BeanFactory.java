package cn.zay.zayboot.core.ioc;

import cn.zay.zayboot.annotation.ioc.Component;
import cn.zay.zayboot.exception.NoSuchBeanDefinitionException;
import cn.zay.zayboot.util.ReflectionUtil;
import cn.zay.zayboot.util.StringUtil;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZAY
 */
public final class BeanFactory {
    /**
     * bean 容器
     * 所有的对象都存放到这个 HashMap里统一管理
     */
    public static final Map<String, Object> BEANS = new ConcurrentHashMap<>(128);
    /**
     * 保存对应包中由指定注解注释的类的集合
     */
    public static final Map<Class<? extends Annotation>, Set<Class<?>>> CLASSES = new ConcurrentHashMap<>(128);

    /**
     * 扫描出指定包路径下被@Component注解的类(后续还会添加更多注解),
     * 并存放到 CLASSES容器中
     * @param packageName 指定的包路径
     */
    public static void loadClass(String[] packageName) {
        Set<Class<?>> components = ReflectionUtil.scanAnnotatedClass(packageName, Component.class);
        CLASSES.put(Component.class, components);
    }

    /**
     * 从 CLASSES容器中拿出被 @Component类注释的类的集合(后续还会添加更多注解),
     * 然后获取到 bean名称，再创建出对象，存入 BEANS容器中
     */
    public static void loadBeans(){
        CLASSES.get(Component.class).forEach(aClass -> {
            String beanName = getBeanName(aClass);
            Object obj = ReflectionUtil.newInstance(aClass);
            BEANS.put(beanName, obj);
        });
    }

    /**
     * 获取指定类的 bean名称(用作 BEANS中的 key),如果其被 @Component注释，就获取注解中的 name
     * @param aClass 指定的目标类
     * @return bean名称
     */
    public static String getBeanName(Class<?> aClass) {
        String beanName = aClass.getName();
        //判断该类有没有被 @Component注释
        if (aClass.isAnnotationPresent(Component.class)) {
            Component component = aClass.getAnnotation(Component.class);
            //如果 @Component后没有设置bean名称,就用类名的首字母小写; 设置了name就用name
            beanName = "".equals(component.name()) ? StringUtil.lowercaseInitials(StringUtil.classPathToClassName(aClass.getName())) : component.name();
        }
        return beanName;
    }
    public static <T> T getBean(Class<T> type) throws NoSuchBeanDefinitionException {
        Object bean=BEANS.get(getBeanName(type));
        if(bean == null){
            throw new NoSuchBeanDefinitionException("找不到这个bean:"+type.getName());
        }
        return type.cast(bean);
    }
    public static Object getBean(String name) throws NoSuchBeanDefinitionException {
        Object bean=BEANS.get(name);
        if(bean == null){
            throw new NoSuchBeanDefinitionException("找不到这个bean:"+name);
        }
        return bean;
    }
}
