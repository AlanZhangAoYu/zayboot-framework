package cn.zay.zayboot.core.ioc;

import cn.zay.zayboot.annotation.aop.Aspect;
import cn.zay.zayboot.annotation.ioc.Component;
import cn.zay.zayboot.annotation.springmvc.RestController;
import cn.zay.zayboot.core.config.ConfigurationManager;
import cn.zay.zayboot.exception.NoSuchBeanDefinitionException;
import cn.zay.zayboot.util.ReflectionUtil;
import cn.zay.zayboot.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZAY
 */
@Slf4j
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
     * 扫描出指定包路径下被 @Component、 @Aspect和 @RestController注释的类(后续还会添加更多注解),并存放到 CLASSES容器中
     * @param packageName 指定的包路径
     */
    public static void loadClass(String[] packageName) {
        Set<Class<?>> components = ReflectionUtil.scanAnnotatedClass(packageName, Component.class);
        Set<Class<?>> aspects = ReflectionUtil.scanAnnotatedClass(packageName, Aspect.class);
        Set<Class<?>> restControllers = ReflectionUtil.scanAnnotatedClass(packageName, RestController.class);
        CLASSES.put(Component.class, components);
        CLASSES.put(Aspect.class, aspects);
        CLASSES.put(RestController.class, restControllers);
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

    /**
     * 自动注入, 扫描 BEANS中所有的 bean对象, 检查该对象中每个属性是否被 @Autowired和 @Value注解, 并自动注入
     * @param packageName @ComponentScan扫描的包路径组
     */
    public static void automaticInjection(String[] packageName) throws Exception{
        //加载自动注入初始化器, 为执行注入方法initialize做准备
        AutowiredBeanInitialization autowiredBeanInitialization=new AutowiredBeanInitialization(packageName);
        ConfigurationManager configurationManager =new ConfigurationManager();
        List<Path> pathList=new ArrayList<>();
        for (String defaultConfigFileName : ConfigurationManager.DEFAULT_CONFIG_FILENAMES) {
            //分别在打包好的根路径下寻找默认配置文件, 若找到就将其添加到 pathList表中
            URL resource = Thread.currentThread().getContextClassLoader().getResource(defaultConfigFileName);
            if(resource != null){
                log.info("找到配置文件[{}]",defaultConfigFileName);
                URI uri=resource.toURI();
                pathList.add(Paths.get(uri));
            }
        }
        //将配置文件加载到bean容器中
        configurationManager.loadResources(pathList);
        //扫描容器中的每一个bean, 判断该bean是否需要注入, 并实施注入
        Set<String> keySet=BEANS.keySet();
        for (String s : keySet) {
            Object obj=getBean(s);
            autowiredBeanInitialization.initialize(obj);
        }
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
