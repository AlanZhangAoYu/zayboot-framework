package cn.zay;

/**
 * 用来在 zayboot-framework框架中测试自动装配的自己写的第三方测试类
 * @author ZAY
 */
public class Example {
    private final String param;
    public Example(){
        param = "测试类成功注入";
    }
    @Override
    public String toString() {
        return this.param;
    }
}
