import cn.zay.zayboot.core.boot.Banner;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class BannerTest {
    @Test
    public void test1(){
        try {
            Banner.print();
        } catch (Exception e) {
            log.error("异常!!",e);
        }
    }
}
