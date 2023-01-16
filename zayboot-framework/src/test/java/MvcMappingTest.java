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
        String uri1 = "/hello?recipient=world&x=1&y=2&z=3";
        String uri2 = "/hello/123456";
        String uri3 = "/hello/{id}";
        String uri4 = "/hello/{id}/getName/{name}";
        String uri5 = "/hello/132456/getName/zhangaoyu";
        log.info("uri1解析后的路径:{}",UrlUtil.getRequestPath(uri1));
        log.info("uri3解析后的路径:{}",UrlUtil.getRequestPath(uri3));
        log.info("uri1解析后的参数:{}",UrlUtil.getUrlParameterMapForCommon(uri1));
        log.info("uri2解析后的参数:{}",UrlUtil.getUrlParameterMapForRestFul(uri2,uri3));
        log.info("uri2解析后的参数:{}",UrlUtil.getUrlParameterMapForRestFul(uri5,uri4));
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
