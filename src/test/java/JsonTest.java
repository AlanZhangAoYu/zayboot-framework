import cn.zay.demo.java.pojo.Student;
import cn.zay.zayboot.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

@Slf4j
public class JsonTest {
    @Test
    public void test1(){
        //解析时参数名 name 必须为 "name", 不然解析失败
        log.debug("{}",JsonUtil.deserialize("{'name': 'XXX', 'age': 15}", Student.class));
        log.debug("{}",JsonUtil.deserialize("{'name': 'XXX', 'age': 15}".getBytes(StandardCharsets.UTF_8), Student.class));
    }
}
