package cn.zay.zayboot.mvc.resolver;

import cn.zay.zayboot.annotation.springmvc.RequestBody;
import cn.zay.zayboot.mvc.MappingMethodDetail;
import cn.zay.zayboot.util.JsonUtil;
import com.alibaba.fastjson2.JSON;

import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;

/**
 * process @RequestBody annotation
 *
 * @author ZAY
 */
public class RequestBodyParameterResolver implements ParameterResolver {

    @Override
    public Object resolve(MappingMethodDetail methodDetail, Parameter parameter) {
        Object param = null;
        RequestBody requestBody = parameter.getDeclaredAnnotation(RequestBody.class);
        if (requestBody != null) {
            //param = JSON.parseObject(methodDetail.getJson(), parameter.getType());
            //param = JsonUtil.deserialize(methodDetail.getJson(), parameter.getType());
        }
        return param;
    }
}
