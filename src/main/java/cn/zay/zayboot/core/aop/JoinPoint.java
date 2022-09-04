package cn.zay.zayboot.core.aop;

/**
 * @author ZAY
 */
public interface JoinPoint {
    /**
     * get point this
     */
    Object getAdviceBean();

    /**
     * get target object
     */
    Object getTarget();

    /**
     * get parameters for object array
     */
    Object[] getArgs();
}
