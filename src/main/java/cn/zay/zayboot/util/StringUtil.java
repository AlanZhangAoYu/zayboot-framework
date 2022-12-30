package cn.zay.zayboot.util;

import cn.zay.zayboot.exception.IllegalParameterFormatException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        throw new IllegalParameterFormatException("传入的参数不合法!["+str+"]必须以'#{}'或'${}'包裹");
    }
    /**
     * 根据给定正则表达式去匹配一个大字符串中符合要求的字符串
     * @param patternStr 要匹配的正则表达式
     * @param str 要匹配的字符串
     * @return 匹配的字符串列表
     */
    public static List<String> simpleMatch(String patternStr, String str) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(str);
        List<String> resultList = new ArrayList<>();
        while (matcher.find()){
            resultList.add(matcher.group());
        }
        return resultList;
    }
}
