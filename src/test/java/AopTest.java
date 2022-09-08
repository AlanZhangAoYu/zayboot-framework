import cn.zay.demo.java.pojo.Student;
import cn.zay.zayboot.core.aop.InterceptorFactory;
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
            InterceptorFactory.INTERCEPTORS.get("cn.zay.demo.java.pojo.People.working").agent();
            InterceptorFactory.runAgentMethod("cn.zay.demo.java.pojo.People.working");
            InterceptorFactory.runAgentMethod("cn.zay.demo.java.pojo.People.haveLunch","12:00");
        }catch (Exception e){
            log.error("异常!",e);
        }
    }
    @Test
    public void test2(){
        Class<Student> aClass= Student.class;
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            log.info(aClass.getName()+"."+method.getName());
        }
    }
}
