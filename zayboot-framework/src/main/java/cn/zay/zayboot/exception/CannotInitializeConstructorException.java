package cn.zay.zayboot.exception;

/**
 * @author ZAY
 * 无法初始化构造函数异常
 */
public class CannotInitializeConstructorException extends Exception{
    public CannotInitializeConstructorException(String message) {
        super(message);
    }
}
