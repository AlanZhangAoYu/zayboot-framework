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
        log.debug(StringUtil.injectionFormatToValuePath("$( xxx.xxx.Xxx) "));
    }
}
