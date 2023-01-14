package application;

import cn.zay.zayboot.annotation.boot.ComponentScan;
import cn.zay.zayboot.annotation.boot.ZayBootApplication;
import cn.zay.zayboot.core.boot.ZayApplication;

/**
 * @author ZAY
 */
@ZayBootApplication
@ComponentScan({"application","pojo","controller"})
public class HahaApplication {
    public static void main(String[] args) {
        ZayApplication.run(HahaApplication.class,args);
    }
}
