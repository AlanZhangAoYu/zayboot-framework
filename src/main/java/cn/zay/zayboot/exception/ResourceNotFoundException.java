package cn.zay.zayboot.exception;

/**
 * 资源未找到异常, 一般要返回 404
 * @author ZAY
 */
public class ResourceNotFoundException extends Exception{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
