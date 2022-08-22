package cn.zay.zayboot.core.ioc;

import cn.zay.zayboot.annotation.config.Value;
import cn.zay.zayboot.annotation.ioc.Autowired;
import cn.zay.zayboot.annotation.ioc.Qualifier;
import cn.zay.zayboot.core.config.ConfigurationManager;
import cn.zay.zayboot.exception.InterfaceNotHaveImplementedClassException;
import cn.zay.zayboot.exception.NoSuchBeanDefinitionException;
import cn.zay.zayboot.util.ReflectionUtil;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author ZAY
 * 处理被注解 @Autowired和 @Value注释的字段
 */
public class AutowiredBeanInitialization {
    private final String[] packageNames;
    public AutowiredBeanInitialization(String[] packageNames) {
        this.packageNames = packageNames;
    }

    /**
     * 处理目标 bean对象中被 @Autowired和 @Value注释的字段
     * 向该字段(属性)注入 BEANS中对应的对象
     * @param beanInstance 目标bean对象
     * @throws Exception 找不到bean异常
     */
    public void initialize(Object beanInstance) throws Exception{
        Class<?> beanClass = beanInstance.getClass();
        Field[] beanFields = beanClass.getDeclaredFields();
        // 遍历bean的属性(字段)
        if (beanFields.length > 0) {
            for (Field beanField : beanFields) {
                if (beanField.isAnnotationPresent(Autowired.class)) {
                    Object beanFieldInstance = processAutowiredAnnotationField(beanField);
                    //String beanFieldName = BeanFactory.getBeanName(beanField.getType());
                    ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                }
            }
        }
    }

    /**
     * 根据 bean中的字段(属性)在 BEANS容器中查找对应 bean, 准备注入
     * @param beanField 目标类的字段
     * @return 目标类的字段对应的对象
     */
    private Object processAutowiredAnnotationField(Field beanField) throws Exception{
        Class<?> beanFieldClass = beanField.getType();
        String beanFieldName = BeanFactory.getBeanName(beanFieldClass);
        //确定此类对象是否表示接口类型
        if (beanFieldClass.isInterface()) {
            //如果此字段为接口类型，获取到此接口的实现类
            //告诉编译器忽略 unchecked 警告信息
            @SuppressWarnings("unchecked")
            Set<Class<?>> subClasses = ReflectionUtil.getSubClass(packageNames, (Class<Object>) beanFieldClass);
            if (subClasses.size() == 0) {
                throw new InterfaceNotHaveImplementedClassException(beanFieldClass.getName() + "是一个接口且没有找到该接口的实现类");
            }
            if (subClasses.size() == 1) {
                Class<?> subClass = subClasses.iterator().next();
                beanFieldName = BeanFactory.getBeanName(subClass);
            }
            if (subClasses.size() > 1) {
                Qualifier qualifier = beanField.getDeclaredAnnotation(Qualifier.class);
                beanFieldName = qualifier == null ? beanFieldName : qualifier.value();
            }
        }
        Object beanFieldInstance = BeanFactory.BEANS.get(beanFieldName);
        if (beanFieldInstance == null) {
            throw new NoSuchBeanDefinitionException("找不到这个bean:" + beanFieldClass.getName());
        }
        return beanFieldInstance;
    }

    /**
     * 处理被 @Value 注解标记的字段
     *
     * @param beanField 目标类的字段
     * @return 目标类的字段对应的对象
     */
    private Object processValueAnnotationField(Field beanField) {
        String key = beanField.getDeclaredAnnotation(Value.class).value();
        ConfigurationManager configurationManager = (ConfigurationManager) BeanFactory.BEANS.get(ConfigurationManager.class.getName());
        String value = configurationManager.getString(key);
        if (value == null) {
            throw new IllegalArgumentException("can not find target value for property:{" + key + "}");
        }
        return ObjectUtil.convert(beanField.getType(), value);
    }
}
