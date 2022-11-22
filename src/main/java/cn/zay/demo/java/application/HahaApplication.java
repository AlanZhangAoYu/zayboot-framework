package cn.zay.demo.java.application;

import cn.zay.zayboot.annotation.boot.ComponentScan;
import cn.zay.zayboot.annotation.boot.ZayBootApplication;
import cn.zay.zayboot.core.boot.ZayApplication;

/**
 * @author ZAY
 */
@ZayBootApplication
@ComponentScan({"cn.zay.demo.java.application","cn.zay.demo.java.pojo","cn.zay.demo.java.controller"})
public class HahaApplication {
    public static void main(String[] args) {
        ZayApplication.run(HahaApplication.class,args);
    }
}
