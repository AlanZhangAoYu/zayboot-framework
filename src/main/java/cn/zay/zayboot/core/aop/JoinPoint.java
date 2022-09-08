package cn.zay.zayboot.core.aop;

/**
 * @author ZAY
 * 切点的实现类
 */
public class JoinPoint{
    /**
     * 被 @Before或 @After注释的方法的执行器
     */
    private final MethodInvocation methodInvocation;
    /**
     * 在 @Before或 @After注解中设置的切点方法名组
     * 例: @Before({"cn.zay.pojo.Student.learn","cn.zay.pojo.Teacher.work"})
     * pointcuts中的记录: {"cn.zay.pojo.Student.learn","cn.zay.pojo.Teacher.work"}
     */
    private final String[] pointcuts;
    public JoinPoint(MethodInvocation methodInvocation, String[] pointcuts) {
        this.methodInvocation = methodInvocation;
        this.pointcuts = pointcuts;
    }
    public MethodInvocation getMethodInvocation() {
        return methodInvocation;
    }
    public String[] getBeforePointcuts() {
        return pointcuts;
    }
}
