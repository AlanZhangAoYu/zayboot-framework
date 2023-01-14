package pojo;

import cn.zay.zayboot.annotation.ioc.Autowired;
import cn.zay.zayboot.annotation.ioc.Component;

/**
 * @author ZAY
 */
@Component
public class ClassA {
    @Autowired
    private ClassB classB;
    public void showInfo(){
        System.out.println(classB);
    }
    @Override
    public String toString() {
        return "我是ClassA, 我依赖了classB";
    }
}
