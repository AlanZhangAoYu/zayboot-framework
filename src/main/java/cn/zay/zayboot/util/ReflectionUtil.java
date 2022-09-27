package cn.zay.zayboot.util;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * 常见的反射方法
 * @author ZAY
 */
@Slf4j
public class ReflectionUtil {
    /**
     * 扫描指定包中由指定注释标记的类
     * @param packageNames 指定的包名称
     * @param annotation 指定的注释
     * @return 指定包中由指定注释标记的类的集合
     */
    public static Set<Class<?>> scanAnnotatedClass(String[] packageNames, Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections(packageNames, new TypeAnnotationsScanner());
        Set<Class<?>> annotatedClass = reflections.getTypesAnnotatedWith(annotation, true);
        log.info("由@{}注释的类的数量为:[{}]",annotation.getSimpleName(),annotatedClass.size());
        return annotatedClass;
    }
    /**
     * 获取接口的实现类
     * @param packageNames 指定的包名称
     * @param interfaceClass 指定的接口
     * @return 指定接口的实现类的集合
     */
    public static <T> Set<Class<? extends T>> getSubClass(Object[] packageNames, Class<T> interfaceClass) {
        Reflections reflections = new Reflections(packageNames);
        return reflections.getSubTypesOf(interfaceClass);
    }
    /**
     * 通过类创建对象实例
     * @param cls 目标类
     * @return 由目标类创建的对象
     */
    public static Object newInstance(Class<?> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("异常!",e);
        }
        return null;
    }
    /**
     * 设置对象中字段的值
     * @param obj 目标对象
     * @param field 目标字段
     * @param value 分配给字段的值
     */
    public static void setField(Object obj, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException impossible) {
            log.error("异常!",impossible);
        }
    }
    /**
     * 执行有返回值的目标方法
     * @param targetObject 目标类
     * @param method 目标方法
     * @param args 方法参数
     * @return 方法执行的结果
     */
    public static Object executeTargetMethod(Object targetObject, Method method, Object... args) {
        try {
            return method.invoke(targetObject, args);
        } catch (Exception e) {
            log.error("异常!",e);
        }
        return null;
    }
    /**
     * 执行没有返回值的 void方法
     * @param targetObject 目标类
     * @param method 目标方法
     * @param args 目标参数
     */
    public static void executeTargetMethodNoResult(Object targetObject, Method method, Object... args) {
        try {
            // invoke target method through reflection
            method.invoke(targetObject, args);
        } catch (Exception e) {
            log.error("异常!",e);
        }
    }
}
