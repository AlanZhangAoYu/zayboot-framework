package cn.zay.zayboot.util;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

/**
 * @author ZAY
 **/
public class ObjectUtil {
    /**
     * 将字符串(一个值)转换为目标类型(用于 @Value注入)
     * 参考: https://juejin.cn/post/6844904047074344967#hdate
     * @param targetType 字符串要转的目标类
     * @param s 要转换的值
     * @throws NumberFormatException 字符串转数字时，若字符串包含非数字字符，抛出异常
     */
    public static Object convert(Class<?> targetType, String s) {
        PropertyEditor editor = PropertyEditorManager.findEditor(targetType);
        editor.setAsText(s);
        return editor.getValue();
    }
}
