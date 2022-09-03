import cn.zay.demo.java.pojo.Student;
import cn.zay.zayboot.core.ioc.BeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
@Slf4j
public class AopTest {
    @Test
    public void jdkTest1(){
        try{
            BeanFactory.loadClass(new String[]{"cn.zay.demo.java.pojo"});
            BeanFactory.loadBeans();
            BeanFactory.automaticInjection(new String[]{"cn.zay.demo.java.pojo"});
            Student student=(Student) BeanFactory.getBean("ZAY");
            log.info(student.toString());
        }catch (Exception e){
            log.error("异常!",e);
        }
    }
}
