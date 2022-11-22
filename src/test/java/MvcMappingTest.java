import cn.zay.zayboot.core.ioc.BeanFactory;
import cn.zay.zayboot.mvc.RouteMethodFactory;
import cn.zay.zayboot.util.DateUtil;
import cn.zay.zayboot.util.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
@Slf4j
public class MvcMappingTest {
    @Test
    public void test1(){
        log.info(UrlUtil.formatUrl("/user/{userId}"));
    }
    @Test
    public void test2(){
        log.info(DateUtil.now());
    }
    @Test
    public void test3(){
        String uri = "/hello?recipient=world&x=1;y=2";
        log.info(UrlUtil.getRequestPath(uri));
        log.info("{}",UrlUtil.getRequestParameters(uri));
    }
    @Test
    public void test6(){
        String[] path = new String[]{"cn.zay.demo.java.controller"};
        try {
            BeanFactory.loadClass(path);
            BeanFactory.loadBeans();
            BeanFactory.automaticInjection(path);
            RouteMethodFactory.loadRoutes();
            log.info("{}",RouteMethodFactory.getRequestMethodMap());
            log.info("{}",RouteMethodFactory.getRequestUrlMap());
        } catch (Exception e) {
            log.error("异常!",e);
        }
    }
}
