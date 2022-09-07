import cn.zay.zayboot.core.aop.InterceptorFactory;
import cn.zay.zayboot.core.aop.MethodInvocation;
import cn.zay.zayboot.core.ioc.BeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

@Slf4j
public class AopTest {
    @Test
    public void test1(){
        try{
            String[] packageNames=new String[]{"cn.zay.demo.java.pojo"};
            BeanFactory.loadClass(packageNames);
            BeanFactory.loadBeans();
            InterceptorFactory.loadInterceptors(packageNames);
            Method method=BeanFactory.getBean("people").getClass().getMethod("working");
            MethodInvocation methodInvocation=new MethodInvocation(BeanFactory.getBean("people"),method,null);
            InterceptorFactory.INTERCEPTORS.get("0").agent(methodInvocation);
        }catch (Exception e){
            log.error("异常!",e);
        }
    }
}
