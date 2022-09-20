package cn.zay.zayboot.util;

import cn.zay.zayboot.exception.IllegalParameterFormatException;

/**
 * 一些涉及字符串的方法
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
        if (chars[0] >= 'A' && chars[0] <= 'Z') {
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
    public static String injectionFormatToValuePath(String str) throws IllegalParameterFormatException{
        str=str.trim();
        if(str.startsWith("#{") || str.startsWith("${")){
            if(str.endsWith("}")){
                return str.substring(2,str.length()-1).trim();
            }
        }
        throw new IllegalParameterFormatException("传入的参数不合法!");
    }
    /**
     * 根据给定模式匹配字符串, 支持以下简单图案样式: "xxx*"、"*xxx*"、"*xxx*"、"xxx*yyy"
     * @param pattern 要匹配的样式
     * @param str 要匹配的字符串
     * @return 字符串与指定样式是否匹配
     */
    public static boolean simpleMatch(String pattern, String str) {
        if (pattern == null || str == null) {
            return false;
        }
        int firstIndex = pattern.indexOf('*');
        if (firstIndex == -1) {
            return pattern.equals(str);
        }
        if (firstIndex == 0) {
            if (pattern.length() == 1) {
                return true;
            }
            int nextIndex = pattern.indexOf('*', firstIndex + 1);
            if (nextIndex == -1) {
                return str.endsWith(pattern.substring(1));
            }
            String part = pattern.substring(1, nextIndex);
            if ("".equals(part)) {
                return simpleMatch(pattern.substring(nextIndex), str);
            }
            int partIndex = str.indexOf(part);
            while (partIndex != -1) {
                if (simpleMatch(pattern.substring(nextIndex), str.substring(partIndex + part.length()))) {
                    return true;
                }
                partIndex = str.indexOf(part, partIndex + 1);
            }
            return false;
        }
        return (str.length() >= firstIndex
                && pattern.substring(0, firstIndex).equals(str.substring(0, firstIndex))
                && simpleMatch(pattern.substring(firstIndex), str.substring(firstIndex)));
    }
    /**
     * 给定一组指定样式, 去匹配字符串
     * @param patterns 要匹配的样式组
     * @param str 要匹配的字符串
     * @return 给定字符串是否与给定样式中的一个匹配
     */
    public static boolean simpleMatch(String[] patterns, String str) {
        if (patterns != null) {
            for (String pattern : patterns) {
                if (simpleMatch(pattern, str)) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 将 {xxx} 占位符替换为匹配中文、英文字母和数字以及下划线的正则表达式
     * for example : "/user/{name}" -> "^/user/[\u4e00-\u9fa5_a-zA-Z0-9]+/?$"
     */
    public static String formatUrl(String url) {
        String originPattern = url.replaceAll("(\\{\\w+})", "[\\\\u4e00-\\\\u9fa5_a-zA-Z0-9]+");
        String pattern = "^" + originPattern + "/?$";
        return pattern.replaceAll("/+", "/");
    }
}
