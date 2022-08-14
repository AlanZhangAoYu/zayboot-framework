package cn.zay.zayboot.annotation.boot;

import java.lang.annotation.*;

/**
 * @author ZAY
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ComponentScan {
    String[] value() default {};
}
