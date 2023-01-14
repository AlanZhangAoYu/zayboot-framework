package cn.zay.zayboot.exception;

/**
 * @author ZAY
 * 接口没有实现类异常
 */
public class InterfaceNotHaveImplementedClassException extends Exception{
    public InterfaceNotHaveImplementedClassException(String message) {
        super(message);
    }
}
