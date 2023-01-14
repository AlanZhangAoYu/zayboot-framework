package cn.zay.zayboot.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ZAY
 * 没有找到你要找的 bean对象
 */
@Slf4j
public class NoSuchBeanDefinitionException extends Exception{
    public NoSuchBeanDefinitionException(String message) {
        super(message);
    }
}
