package cn.zay.zayboot.annotation.boot;

import java.lang.annotation.*;

/**
 * @author ZAY
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ComponentScan
public @interface SpringBootApplication {

}
