import cn.zay.zayboot.annotation.ioc.Component;
import cn.zay.zayboot.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {
    @Test
    public void test1(){
        log.debug(ReflectionUtil.scanAnnotatedClass(new String[]{"cn.zay.demo.java.pojo"}, Component.class).toString());
        try {
            Object student =ReflectionUtil.newInstance(Class.forName("cn.zay.demo.java.pojo.Student"));
            Class<?> personClass=student.getClass();
            Field[] fields= personClass.getDeclaredFields();
            if(fields.length > 0){
                for(Field field : fields ){
                    log.debug("对象person中有[{}]字段",field);
                }
            }
        } catch (ClassNotFoundException e) {
            log.error("异常!",e);
        }
    }
    @Test
    public void test2(){
        try {
            Object student =ReflectionUtil.newInstance(Class.forName("cn.zay.demo.java.pojo.Student"));
            Method method=student.getClass().getMethod("sayHello");
            ReflectionUtil.executeTargetMethod(student,method);
        } catch (Exception e) {
            log.error("异常!",e);
        }
    }
}
