package cn.zay.zayboot.util;

import java.util.Arrays;

/**
 * @author ZAY
 */
public class StringUtil {
    /**
     * 将首字母大写的类名转化为首字母小写 (StudentService -> studentService)
     * @param str 首字母大写的默认类名
     * @return 转换后的首字母小写的类名
     */
    public static String lowercaseInitials(String str){
        str=str.trim();
        char[] chars=str.toCharArray();
        if (chars[0] >=65 && chars[0] <= 90) {
            //首字母为大写则转换, 为小写则不变, 原样输出
            chars[0]= (char) (chars[0]+('a'-'A'));
        }
        return String.valueOf(chars);
    }
    /**
     * 类路径转为类名, 例:cn.zay.demo.java.pojo.StudentAutowired -> StudentAutowired
     * @param str 默认的类路径
     * @return 转换后的类名
     */
    public static String classPathToClassName(String str){
        String[] strings=str.split("\\.");
        return strings[strings.length-1];
    }
    /**
     * 将 "#{xxx.xxx.Xxx}" 或 "${xxx.xxx.Xxx}" 转换为 "xxx.xxx.Xxx"
     * @param str 输入的格式
     * @return 转换后的值
     */
    public static String injectionFormatToValuePath(String str){
        str=str.trim();
        return str.substring(2,str.length()-1).trim();
    }
}
