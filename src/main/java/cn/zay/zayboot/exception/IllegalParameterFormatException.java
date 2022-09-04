package cn.zay.zayboot.exception;

/**
 * @author ZAY
 * 非法参数格式异常
 */
public class IllegalParameterFormatException extends Exception{
    public IllegalParameterFormatException(String message) {
        super(message);
    }
}
