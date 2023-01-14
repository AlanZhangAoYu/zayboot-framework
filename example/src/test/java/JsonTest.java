import cn.zay.zayboot.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import pojo.Student;

import java.nio.charset.StandardCharsets;

@Slf4j
public class JsonTest {
    @Test
    public void test1(){
        //解析时参数名 name 必须为 "name", 不然解析失败
        log.debug("{}",JsonUtil.deserialize("{'name': 'XXX', 'age': 15}", Student.class));
        log.debug("{}",JsonUtil.deserialize("{'name': 'XXX', 'age': 15}".getBytes(StandardCharsets.UTF_8), Student.class));
    }
    @Test
    public void test2(){
        log.info("{}",JsonUtil.serialize(new Student()));
    }
    @Test
    public void test3(){
        byte[] bytes = new byte[]{123, 34, 97, 103, 101, 34, 58, 48, 44, 34, 110, 97, 109, 101, 34, 58, 34, 90, 65, 89, 34, 125};
        log.info("{}",JsonUtil.deserialize(bytes, Student.class));
    }
}
