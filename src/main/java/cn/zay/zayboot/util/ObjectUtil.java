package cn.zay.zayboot.util;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

/**
 * @author ZAY
 **/
public class ObjectUtil {
    /**
     * convert from String to a target type
     *
     * @param targetType the type to be converted
     * @param s          the string to be converted
     * @throws NumberFormatException When string to number, if string is not a number,then throw NumberFormatException
     */
    public static Object convert(Class<?> targetType, String s) {
        PropertyEditor editor = PropertyEditorManager.findEditor(targetType);
        editor.setAsText(s);
        return editor.getValue();
    }
}
