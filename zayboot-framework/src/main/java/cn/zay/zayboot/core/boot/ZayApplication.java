package cn.zay.zayboot.core.boot;

import cn.zay.zayboot.annotation.boot.ComponentScan;
import cn.zay.zayboot.core.aop.InterceptorFactory;
import cn.zay.zayboot.core.ioc.BeanFactory;
import cn.zay.zayboot.mvc.RouteMethodFactory;
import cn.zay.zayboot.server.HttpServer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZAY
 * zayboot-framework启动器
 */
@Slf4j
public class ZayApplication {
    public static void run(Class<?> applicationClass,String[] args){
        try{
            //加载 banner.txt
            Banner.print();
            String[] packageNames;
            if(applicationClass.isAnnotationPresent(ComponentScan.class)){
                //如果 applicationClass被 @ComponentScan注释, 获取 @ComponentScan中的值, 得到要扫描的包路径组
                packageNames = ComponentScanner.getPackageNames(applicationClass);
            }else {
                //如果没有被 @ComponentScan注释, 就获取 applicationClass所在的包路径
                packageNames = new String[]{applicationClass.getPackage().getName()};
            }
            //加载 bean到 BEANS容器中
            BeanFactory.loadClass(packageNames);
            BeanFactory.loadBeans();
            //扫描 @Autowired与 @Value,自动注入
            BeanFactory.automaticInjection(packageNames);
            //加载 Aop拦截器
            InterceptorFactory.loadInterceptors(packageNames);
            //加载 MVC控制器
            RouteMethodFactory.loadRoutes();
            log.info("BEANS容器中的内容:{}",BeanFactory.BEANS);
            log.info("CLASSES容器中的内容:{}",BeanFactory.CLASSES);
            log.info("REQUEST_METHOD_MAP容器中的内容:{}", RouteMethodFactory.getRequestMethodMap());
            log.info("REQUEST_URL_MAP容器中的内容:{}", RouteMethodFactory.getRequestUrlMap());
            //启动 Netty服务器
            HttpServer.start();
        }catch (Exception e){
            log.error("异常",e);
        }
    }
}
