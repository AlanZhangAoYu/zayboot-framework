package cn.zay.demo.java.pojo;

import cn.zay.zayboot.annotation.aop.After;
import cn.zay.zayboot.annotation.aop.Aspect;
import cn.zay.zayboot.annotation.aop.Before;
import cn.zay.zayboot.annotation.aop.Pointcut;
import cn.zay.zayboot.annotation.ioc.Component;
import lombok.extern.slf4j.Slf4j;
/**
 * @author ZAY
 */
@Aspect
@Component
@Slf4j
public class People {
    @Before({"cn.zay.demo.java.pojo.People.working"})
    public void goToWork(){
        log.info("上班打卡");
    }
    @Pointcut
    public void working(){
        log.info("人在上班");
    }
    @After({"cn.zay.demo.java.pojo.People.working"})
    public void goOffWork(){
        log.info("下班打卡");
    }
}
