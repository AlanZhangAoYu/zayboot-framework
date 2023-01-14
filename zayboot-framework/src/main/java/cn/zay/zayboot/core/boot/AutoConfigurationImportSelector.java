package cn.zay.zayboot.core.boot;

import cn.zay.zayboot.annotation.boot.AutomaticAnjection;
import java.net.URL;
import java.util.List;

/**
 * 实现自动装配
 * 参考: https://zhuanlan.zhihu.com/p/384540393
 * @author ZAY
 */
public class AutoConfigurationImportSelector {
    /**
     * 一般没有查到任何要装配的类或者自动装配没有开启时返回该空字符串组
     */
    private static final String[] NO_IMPORTS = new String[0];
    /**
     * 获取所有符合条件的类的全限定类名，这些类需要被加载到 IoC容器中(调用 BeanFactory.loadClass())
     * @return 需要加载到容器中的类的全限定类名的集合
     */
    public static String[] selectImports(Class<?> applicationClass){
        if(!applicationClass.isAnnotationPresent(AutomaticAnjection.class)){
            return NO_IMPORTS;
        }
        return getCandidateConfigurations(applicationClass).toArray(new String[0]);
    }
    /**
     * 通过扫描引用的包中的 META-INF/spring.factories中配好的的自动配置类, 获取候选配置
     * @return 自动配置类的全限定名的集合
     */
    private static List<String> getCandidateConfigurations(Class<?> applicationClass){
        URL resource = applicationClass.getResource("META-INF/spring.factories");
        return null;
    }
}
