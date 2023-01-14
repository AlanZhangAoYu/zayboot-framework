import cn.zay.zayboot.core.ioc.AutowiredBeanInitialization;
import cn.zay.zayboot.core.ioc.BeanFactory;
import cn.zay.zayboot.exception.NoSuchBeanDefinitionException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import pojo.ClassA;
import pojo.ClassB;
import pojo.Student;
import pojo.StudentAutowired;

@Slf4j
public class BeanFactoryTest {
    @Test
    public void test1(){
        BeanFactory.loadClass(new String[]{"pojo"});
        log.debug(BeanFactory.CLASSES.toString());
    }
    @Test
    public void test2(){
        try {
            //这里为什么不能用Class.forName("pojo.Student")
            log.debug(BeanFactory.getBeanName(Student.class));
        } catch (Exception e) {
            log.error("异常",e);
        }
    }
    @Test
    public void test3(){
        BeanFactory.loadClass(new String[]{"pojo"});
        BeanFactory.loadBeans();
        log.debug(BeanFactory.BEANS.toString());
    }
    @Test
    public void test4(){
        try {
            BeanFactory.loadClass(new String[]{"pojo"});
            BeanFactory.loadBeans();
            log.debug(BeanFactory.getBean("ZAY").toString());
        } catch (NoSuchBeanDefinitionException e) {
            log.error("异常",e);
        }
    }
    @Test
    public void test5(){
        try {
            BeanFactory.loadClass(new String[]{"pojo"});
            BeanFactory.loadBeans();
            StudentAutowired studentAutowired=(StudentAutowired) BeanFactory.getBean("studentAutowired");
            AutowiredBeanInitialization autowiredBeanInitialization = new AutowiredBeanInitialization(new String[]{"cn.zay.demo.java.pojo"});
            autowiredBeanInitialization.initialize(studentAutowired);
            log.debug(BeanFactory.BEANS.toString());
            studentAutowired.ZAY.sayHello();
        } catch (Exception e) {
            log.error("异常",e);
        }
    }
    @Test
    public void test6(){
        try{
            BeanFactory.loadClass(new String[]{"pojo"});
            BeanFactory.loadBeans();
            BeanFactory.automaticInjection(new String[]{"pojo"});
            log.debug(BeanFactory.BEANS.toString());
        }catch (Exception e){
            log.error("异常",e);
        }
    }
    @Test
    public void test7(){
        try{
            BeanFactory.loadClass(new String[]{"pojo"});
            BeanFactory.loadBeans();
            BeanFactory.automaticInjection(new String[]{"pojo"});
            ClassA classA=(ClassA) BeanFactory.getBean("classA");
            ClassB classB=(ClassB) BeanFactory.getBean("classB");
            classA.showInfo();
            classB.showInfo();
        }catch (Exception e){
            log.error("异常",e);
        }
    }
}
