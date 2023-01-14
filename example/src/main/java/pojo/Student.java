package pojo;

import cn.zay.zayboot.annotation.aop.After;
import cn.zay.zayboot.annotation.aop.Aspect;
import cn.zay.zayboot.annotation.aop.Pointcut;
import cn.zay.zayboot.annotation.config.Value;
import cn.zay.zayboot.annotation.ioc.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZAY
 */
@Slf4j
@Aspect
@Component(name = "ZAY")
public class Student{
    @Value("${student.name}")
    private String name;
    @Value("${student.age}")
    private int age;
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @After({"pojo.People.working"})
    public void sayHello(){
        log.info("Hello,我是学生{}.",getName());
    }
    public void eat() {
        log.info("学生{}吃饭",name);
    }
    public void sleep(){
        log.info("学生{}在睡觉",name);
    }
    @Override
    @Pointcut
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
