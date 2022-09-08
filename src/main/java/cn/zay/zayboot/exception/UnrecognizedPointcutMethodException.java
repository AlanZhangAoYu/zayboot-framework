package cn.zay.zayboot.exception;

/**
 * @author ZAY
 * 未能识别的 Pointcut方法的异常
 */
public class UnrecognizedPointcutMethodException extends Exception{
    public UnrecognizedPointcutMethodException(String message) {
        super(message);
    }
}
