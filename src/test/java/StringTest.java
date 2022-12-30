import cn.zay.zayboot.exception.IllegalParameterFormatException;
import cn.zay.zayboot.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;


/**
 * @author ZAY
 */
@Slf4j
public class StringTest {
    @Test
    public void test1(){
        log.debug(StringUtil.lowercaseInitials("TeacherServer"));
    }
    @Test
    public void test2(){
        log.debug(StringUtil.classPathToClassName("cn.zay.demo.java.pojo.StudentAutowired"));
    }
    @Test
    public void test3(){
        try {
            log.debug(StringUtil.injectionFormatToValuePath("$( xxx.xxx.Xxx} "));
        } catch (IllegalParameterFormatException e) {
            log.error("异常!",e);
        }
    }
    @Test
    public void test4(){
        log.debug("{}",StringUtil.simpleMatch("\\$\\{[a-zA-Z0-9]+\\}",
                "select * from order where createUser = ${createUser} and orderId = ${orderId}"));
    }
}
