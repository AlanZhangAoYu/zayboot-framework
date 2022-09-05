package cn.zay.zayboot.core.aop;

/**
 * @author ZAY
 * 切点
 */
public interface JoinPoint {
    /**
     * 获取通知 bean
     */
    Object getAdviceBean();

    /**
     * get target object
     */
    Object getTarget();

    /**
     * 获取切点对象的参数
     */
    Object[] getArgs();
}
