package cn.zay.zayboot.core.boot;

import cn.zay.zayboot.core.config.ConfigurationManager;
import cn.zay.zayboot.core.ioc.AutowiredBeanInitialization;
import cn.zay.zayboot.core.ioc.BeanFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZAY
 * zayboot-framework启动器
 */
@Slf4j
public class ZayApplication {
    public static void run(Class<?> applicationClass,String[] args){
        //加载 banner.txt
        Banner.print();
        //获取 @ComponentScan中的值, 得到要扫描的包路径组
        String[] packageNames = ComponentScanner.getPackageNames(applicationClass);
        //加载 bean到 BEANS容器中
        BeanFactory.loadClass(packageNames);
        BeanFactory.loadBeans();
        //处理 @Autowired, 注入对象
        AutowiredBeanInitialization autowiredBeanInitialization = new AutowiredBeanInitialization(packageNames);
        //处理 @Value, 注入值
        ConfigurationManager.loadResources(ZayApplication.class);
        log.info(BeanFactory.BEANS.toString());
    }
}
