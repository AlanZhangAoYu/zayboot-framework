package cn.zay.demo.java.pojo;

import cn.zay.zayboot.annotation.ioc.Autowired;
import cn.zay.zayboot.annotation.ioc.Component;

/**
 * @author ZAY
 */
@Component
public class ClassB {
    @Autowired
    private ClassA classA;
    public void showInfo(){
        System.out.println(classA);
    }
    @Override
    public String toString() {
        return "我是ClassB, 我依赖了ClassA";
    }
}
