import cn.zay.zayboot.core.aop.InterceptorFactory;
import cn.zay.zayboot.core.aop.cglib.CglibProxyFactory;
import cn.zay.zayboot.core.ioc.BeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import pojo.People;
import pojo.Student;

import java.lang.reflect.Method;

@Slf4j
public class AopTest {
    @Test
    public void test1(){
        try{
            String[] packageNames=new String[]{"pojo"};
            BeanFactory.loadClass(packageNames);
            BeanFactory.loadBeans();
            InterceptorFactory.loadInterceptors(packageNames);
            InterceptorFactory.INTERCEPTORS.get("pojo.People.working").agent();
            InterceptorFactory.runProxyMethodWithReturnValueOfVoid("pojo.People.working");
            InterceptorFactory.runProxyMethodWithReturnValueOfVoid("pojo.People.haveLunch", "12:00");
            log.info((String)InterceptorFactory.runProxyMethodWhoseReturnValueIsNotVoid("pojo.Student.toString"));
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
    @Test
    public void test3(){
        try {
            String[] packageNames=new String[]{"pojo"};
            BeanFactory.loadClass(packageNames);
            BeanFactory.loadBeans();
            InterceptorFactory.loadInterceptors(packageNames);
            People people = (People) CglibProxyFactory.getProxy(People.class);
            people.working();
            people.haveLunch("12:00");
        }catch (Exception e){
            log.error("异常!",e);
        }
    }
}
