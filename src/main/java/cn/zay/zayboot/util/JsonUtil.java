package cn.zay.zayboot.util;

import com.alibaba.fastjson2.JSON;

/**
 * json消息序列及反系列化工具
 * @author ZAY
 */
public class JsonUtil {
    /**
     * 将 obj对象序列化为 byte[]
     * @param obj 要序列化的对象
     * @return 序列化后的字节组
     */
    public static byte[] serialize(Object obj){
        return JSON.toJSONBytes(obj);
    }
    /**
     * 将字节组转化为给定的类的对象
     * @param bytes 要转化的字节组
     * @param clazz 给定的要转化成的类
     * @param <T> 转化后的对象所属的类
     * @return 转化后的对象所属的类
     */
    public static <T> T deserialize(byte[] bytes, Class<T> clazz){
        return JSON.parseObject(bytes, clazz);
    }
}
