import cn.zay.zayboot.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

@Slf4j
public class ReflectionTest {
    @Test
    public void test1(){
        log.debug(ReflectionUtil.scanAnnotatedClass(new String[]{"cn.zay.zayboot.util"},Slf4j.class).toString());
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
            log.error("异常:[{}]",e.getMessage());
        }
    }
}
