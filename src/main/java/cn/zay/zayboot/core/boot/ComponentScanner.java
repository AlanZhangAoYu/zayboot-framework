package cn.zay.zayboot.core.boot;

import cn.zay.zayboot.annotation.boot.ComponentScan;
import java.util.Objects;

/**
 * 实现 @ComponentScan
 * @author ZAY
 */
public class ComponentScanner {
    /**
     * 返回 @ComponentScan中设置的值,若没有则返回被注释的类的包路径
     * @param applicationClass 被注释的类
     * @return 包路径组
     */
    public static String[] getPackageNames(Class<?> applicationClass) {
        ComponentScan componentScan = applicationClass.getAnnotation(ComponentScan.class);
        return componentScan != null ? componentScan.value()
                : new String[]{applicationClass.getPackage().getName()};
    }
}
