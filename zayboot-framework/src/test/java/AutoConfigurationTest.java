import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class AutoConfigurationTest {
    @Test
    public void test1(){
        log.debug("{}",this.getClass().getResource("/META-INF/spring.factories"));
    }
}
