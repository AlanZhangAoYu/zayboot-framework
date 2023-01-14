package cn.zay.zayboot.exception;

/**
 * 未找到键对应的值异常
 * @author ZAY
 */
public class NotFoundTheValueCorrespondingToTheKeyException extends Exception{
    public NotFoundTheValueCorrespondingToTheKeyException(String message) {
        super(message);
    }
}
