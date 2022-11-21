import cn.zay.demo.java.pojo.Student;
import cn.zay.zayboot.core.ioc.BeanFactory;
import cn.zay.zayboot.mvc.RouteMethodFactory;
import cn.zay.zayboot.util.DateUtil;
import cn.zay.zayboot.util.JsonUtil;
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
        log.info(UrlUtil.getRequestPath("/user/1"));
    }
    @Test
    public void test4(){
        log.info("{}",JsonUtil.serialize(new Student()));
    }
    @Test
    public void test5(){
        // test4的运行结果
        byte[] bytes = new byte[]{123, 34, 97, 103, 101, 34, 58, 48, 44, 34, 110, 97, 109, 101, 34, 58, 34, 90, 65, 89, 34, 125};
        log.info("{}",JsonUtil.deserialize(bytes, Student.class));
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
