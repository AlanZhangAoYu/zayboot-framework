package cn.zay.zayboot.core.aop;

import cn.zay.zayboot.exception.UnrecognizedPointcutMethodException;
import java.util.ArrayList;
import java.util.List;
/**
 * @author ZAY
 * 拦截器的实现类
 */
public class InternallyAspectInterceptor extends Interceptor{
    /**
     * 要被代理的方法(被 @Pointcut注释的方法)的执行器
     */
    private final MethodInvocation methodInvocation;
    /**
     * 用来存放所有要切入被代理方法之前的方法的执行器 (被 @Before注释的方法)
     */
    private final List<MethodInvocation> beforeMethods = new ArrayList<>();
    /**
     * 用来存放所有要切入被代理方法之后的方法的执行器 (被 @After注释的方法)
     */
    private final List<MethodInvocation> afterMethods = new ArrayList<>();
    public InternallyAspectInterceptor(MethodInvocation methodInvocation) throws Exception{
        this.methodInvocation = methodInvocation;
        init();
    }
    /**
     * 寻找与要被代理的 Pointcut方法匹配的所有 @Before和 @After并将其存入对应的列表中以备后续使用
     */
    private void init() throws Exception{
        for (String s : InterceptorFactory.BEFORE_METHODS_MAP.keySet()) {
            for (String beforePointcut : InterceptorFactory.BEFORE_METHODS_MAP.get(s).getBeforePointcuts()) {
                if(InterceptorFactory.POINTCUT_METHODS_MAP.get(beforePointcut) != null){
                    if((methodInvocation.getTargetObject().getClass().getName()+"."+
                            methodInvocation.getTargetMethod().getName()).equals(beforePointcut)){
                        beforeMethods.add(InterceptorFactory.BEFORE_METHODS_MAP.get(s).getMethodInvocation());
                    }
                }else{
                    throw new UnrecognizedPointcutMethodException("未能识别该["+beforePointcut+"]Pointcut方法, 请确任该方法存在且被@Pointcut注释");
                }
            }
        }
        for (String s : InterceptorFactory.AFTER_METHODS_MAP.keySet()) {
            for (String beforePointcut : InterceptorFactory.AFTER_METHODS_MAP.get(s).getBeforePointcuts()) {
                if(InterceptorFactory.POINTCUT_METHODS_MAP.get(beforePointcut) != null){
                    if((methodInvocation.getTargetObject().getClass().getName()+"."+
                            methodInvocation.getTargetMethod().getName()).equals(beforePointcut)){
                        afterMethods.add(InterceptorFactory.AFTER_METHODS_MAP.get(s).getMethodInvocation());
                    }
                }else{
                    throw new UnrecognizedPointcutMethodException("未能识别该["+beforePointcut+"]Pointcut方法, 请确任该方法存在且被@Pointcut注释");
                }
            }
        }
    }
    @Override
    public boolean supports(Object bean) {
        return false;
    }
    @Override
    public Object agent(MethodInvocation methodInvocation) {
        return null;
    }
    /**
     * 在方法执行器 methodInvocation前后执行 beforeMethods和 afterMethods中的方法, 完成 aop代理
     * @return 返回原本被代理的方法的方法执行器的执行结果
     */
    @Override
    public Object agent(){
        for (MethodInvocation beforeMethod : beforeMethods) {
            beforeMethod.run();
        }
        Object result = methodInvocation.run();
        for (MethodInvocation afterMethod : afterMethods) {
            afterMethod.run();
        }
        return result;
    }
}
