package cn.zay.zayboot.core.ioc;

import cn.zay.zayboot.annotation.config.Value;
import cn.zay.zayboot.annotation.ioc.Autowired;
import cn.zay.zayboot.annotation.ioc.Qualifier;
import cn.zay.zayboot.core.config.ConfigurationManager;
import cn.zay.zayboot.exception.InterfaceNotHaveImplementedClassException;
import cn.zay.zayboot.exception.NoSuchBeanDefinitionException;
import cn.zay.zayboot.exception.NotFoundTheValueCorrespondingToTheKeyException;
import cn.zay.zayboot.util.ObjectUtil;
import cn.zay.zayboot.util.ReflectionUtil;
import cn.zay.zayboot.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZAY
 * 处理被注解 @Autowired和 @Value注释的字段
 */
@Slf4j
public class AutowiredBeanInitialization {
    private final String[] packageNames;
    /**
     * 二级缓存 (解决循环依赖问题)
     */
    private static final Map<String, Object> SINGLETON_OBJECTS = new ConcurrentHashMap<>(64);

    public AutowiredBeanInitialization(String[] packageNames) {
        this.packageNames = packageNames;
    }

    /**
     * 处理目标 bean对象中被 @Autowired、 @Value和 @Qualifier注释的字段
     * 向该字段(属性)注入 BEANS中对应的对象
     * @param beanInstance 目标 bean对象
     * @throws Exception 找不到 bean异常
     */
    public void initialize(Object beanInstance) throws Exception{
        Class<?> beanClass = beanInstance.getClass();
        Field[] beanFields = beanClass.getDeclaredFields();
        // 遍历bean的属性(字段)
        if (beanFields.length > 0) {
            for (Field beanField : beanFields) {
                if (beanField.isAnnotationPresent(Autowired.class)) {
                    Object beanFieldInstance = processAutowiredAnnotationField(beanField);
                    String beanFieldName = BeanFactory.getBeanName(beanField.getType());
                    // 解决循环依赖问题
                    //beanFieldInstance = resolveCircularDependency(beanInstance, beanFieldInstance, beanFieldName);
                    ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                }
                if (beanField.isAnnotationPresent(Value.class)){
                    Object beanFieldInstance = processValueAnnotationField(beanField);
                    ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                }
                if (beanField.isAnnotationPresent(Qualifier.class)){
                    Object beanFieldInstance = processQualifierAnnotationField(beanField);
                    ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                }
            }
        }
    }

    /**
     * 根据 bean中的字段(属性)在 BEANS容器中查找对应 bean, 准备注入
     * @param beanField 要注入的字段
     * @return 要注入的字段对应的对象
     * @throws Exception 可能出现找不到 bean异常
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
        //该方法的核心语句
        Object beanFieldInstance = BeanFactory.BEANS.get(beanFieldName);
        if (beanFieldInstance == null) {
            throw new NoSuchBeanDefinitionException("找不到这个bean:" + beanFieldClass.getName());
        }
        return beanFieldInstance;
    }

    /**
     * 根据 @Qualifier中的值(bean名称)在 BEANS中查找指定的 bean对象, 准备注入
     * @param beanFiled 要注入的字段
     * @return 要注入的字段对应的对象
     * @throws Exception 可能出现找不到 bean异常
     */
    private Object processQualifierAnnotationField(Field beanFiled) throws Exception{
        Qualifier qualifier=beanFiled.getAnnotation(Qualifier.class);
        String beanName = qualifier.value();
        //该方法的核心语句
        Object beanFieldInstance = BeanFactory.BEANS.get(beanName);
        if (beanFieldInstance == null) {
            throw new NoSuchBeanDefinitionException("找不到这个bean:" + beanName);
        }
        return beanFieldInstance;
    }

    /**
     * 根据 @Value注解的 key在配置文件中寻找对应的值, 准备注入
     * @param beanField 目标类的字段
     * @return 目标类的字段对应的对象
     * @throws Exception 可能出现找不到 bean异常
     */
    private Object processValueAnnotationField(Field beanField) throws Exception{
        //此时获取到的key的格式为 "${xxx.xxx.Xxx}"
        String key = beanField.getDeclaredAnnotation(Value.class).value();
        String value = null;
        for (String defaultConfigFileName : ConfigurationManager.DEFAULT_CONFIG_FILENAMES) {
            if(BeanFactory.BEANS.get(defaultConfigFileName) != null){
                Map<String,String> map=(Map<String, String>) BeanFactory.BEANS.get(defaultConfigFileName);
                value = map.get(StringUtil.injectionFormatToValuePath(key));
                if (value == null) {
                    throw new NotFoundTheValueCorrespondingToTheKeyException("不能找到"+key+"的值!");
                }
            }
        }
        return ObjectUtil.convert(beanField.getType(), value);
    }

    /**
     * 二级缓存解决循环依赖问题
     * @param beanInstance 要实施注入的 bean对象
     * @param beanFieldInstance 要注入的值或对象
     * @param beanFieldName 当前要注入的字段的名称
     * @return 要注入的值或对象
     */
    private Object resolveCircularDependency(Object beanInstance, Object beanFieldInstance, String beanFieldName) throws Exception{
        if (SINGLETON_OBJECTS.containsKey(beanFieldName)) {
            beanFieldInstance = SINGLETON_OBJECTS.get(beanFieldName);
        } else {
            SINGLETON_OBJECTS.put(beanFieldName, beanFieldInstance);
        }
        return beanFieldInstance;
    }
}
