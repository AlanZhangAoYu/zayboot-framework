package cn.zay.demo.java.pojo;

import cn.zay.zayboot.annotation.ioc.Component;
import lombok.extern.slf4j.Slf4j;
/**
 * @author ZAY
 */
@Slf4j
@Component(name = "ZAY")
public class Student{
    private String name;
    private int age;
    public Student(){
        this.name = "ZAY";
        this.age = 0;
    }
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
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
