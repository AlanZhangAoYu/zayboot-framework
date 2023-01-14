package cn.zay.zayboot.annotation.boot;

import java.lang.annotation.*;

/**
 * 注释在启动类上表示启动自动注入, 未注释则代表不启动自动注解
 * @author ZAY
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutomaticAnjection {
}
