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
    @Before({"cn.zay.demo.java.pojo.People.working","cn.zay.demo.java.pojo.People.haveLunch"})
    public void goToWork(){
        log.info("上班打卡");
    }
    @Pointcut
    public void working(){
        log.info("人在上班");
    }
    @Pointcut
    public void haveLunch(String time){
        log.info("人在"+time+"吃午饭");
    }
    @After({"cn.zay.demo.java.pojo.People.working","cn.zay.demo.java.pojo.People.haveLunch"})
    public void goOffWork(){
        log.info("下班打卡");
    }
}
