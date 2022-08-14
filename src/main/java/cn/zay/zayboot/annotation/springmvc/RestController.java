package cn.zay.zayboot.annotation.springmvc;

import cn.zay.zayboot.annotation.ioc.Component;
import java.lang.annotation.*;

/**
 * @author ZAY
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestController {
    String value() default "";
}
