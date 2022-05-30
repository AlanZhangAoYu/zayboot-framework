package cn.zay.zayboot.util;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author ZAY
 * 反射工具类
 * 基于org.reflections
 */
@Slf4j
public class ReflectionUtil {
    /**
     * 扫描指定包中由指定注释标记的类
     * @param packageNames 指定的包路径
     * @param annotation   指定的注解名称
     * @return 指定包中由指定注释标记的类
     */
    public static Set<Class<?>> scanAnnotatedClass(String[] packageNames, Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections(packageNames, new TypeAnnotationsScanner());
        Set<Class<?>> annotatedClass = reflections.getTypesAnnotatedWith(annotation, true);
        log.info("注解: @" + annotation.getSimpleName() +"在"+packageNames+ "中的数量为:[{}]", annotatedClass.size());
        return annotatedClass;
    }
    /**
     * 获取接口的实现类
     * @param packageNames   指定的包名称
     * @param interfaceClass 指定的接口
     */
    public static <T> Set<Class<? extends T>> getSubClass(Object[] packageNames, Class<T> interfaceClass) {
        Reflections reflections = new Reflections(packageNames);
        return reflections.getSubTypesOf(interfaceClass);
    }
    /**
     * 通过类创建对象实例
     * @param cls 要创建对象的类
     * @return 创建出来的实例对象
     */
    public static Object newInstance(Class<?> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
    /**
     * set the value of a field in the object
     *
     * @param obj   target object
     * @param field target field
     * @param value the value assigned to the field
     */
    public static void setField(Object obj, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException impossible) {
            throw new AssertionError(impossible);
        }

    }

    /**
     * execute the target method
     *
     * @param method target method
     * @param args   method parameters
     * @return the result of method execution
     */
    public static Object executeTargetMethod(Object targetObject, Method method, Object... args) {
        try {
            return method.invoke(targetObject, args);
        } catch (Throwable t) {
            if (t.getCause() != null && t.getCause() instanceof ConstraintViolationException) {
                throw (ConstraintViolationException) t.getCause();
            }
        }
        return null;
    }

    /**
     * execute the void method
     *
     * @param method target method
     * @param args   method parameters
     */
    public static void executeTargetMethodNoResult(Object targetObject, Method method, Object... args) {
        try {
            // invoke target method through reflection
            method.invoke(targetObject, args);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
    }
}
