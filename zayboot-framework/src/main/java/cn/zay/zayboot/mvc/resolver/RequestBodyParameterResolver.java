package cn.zay.zayboot.mvc.resolver;

import cn.zay.zayboot.annotation.springmvc.RequestBody;
import cn.zay.zayboot.util.JsonUtil;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * process @RequestBody annotation
 *
 * @author ZAY
 */
public class RequestBodyParameterResolver implements ParameterResolver {
    @Override
    public Object resolve(Parameter parameter, Map<String,Object> map) {
        Object param = null;
        RequestBody requestBody = parameter.getDeclaredAnnotation(RequestBody.class);
        if (requestBody != null) {
            param = JsonUtil.deserialize(JsonUtil.serialize(map), parameter.getType());
        }
        return param;
    }
}
