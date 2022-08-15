package cn.zay.demo.java.pojo;

import cn.zay.zayboot.annotation.ioc.Autowired;
import cn.zay.zayboot.annotation.ioc.Component;

@Component
public class StudentAutowired {
    @Autowired
    Student ZAY;
    @Override
    public String toString() {
        return "StudentAutowired{" +
                "ZAY=" + ZAY +
                '}';
    }
}
