package cn.zay.zayboot.annotation.springmvc;

import cn.zay.zayboot.annotation.ioc.Component;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RestController {
    String value() default "";
}
