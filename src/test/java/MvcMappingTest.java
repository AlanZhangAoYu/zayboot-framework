import cn.zay.zayboot.util.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
@Slf4j
public class MvcMappingTest {
    @Test
    public void test1(){
        log.info(UrlUtil.formatUrl("/user/{name}"));
    }
    @Test
    public void test2(){
        log.info(UrlUtil.getRequestPath(""));
    }
}
