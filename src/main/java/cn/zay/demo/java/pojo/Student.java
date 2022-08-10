package cn.zay.demo.java.pojo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ZAY
 */
@Slf4j
public class Student {
    private String name;
    private int age;
    public Student(){
        this.name = "";
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
        log.info("Hello,我是学生.");
    }
    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
