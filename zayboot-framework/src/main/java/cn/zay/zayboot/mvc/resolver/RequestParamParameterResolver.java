package cn.zay.zayboot.mvc.resolver;

import cn.zay.zayboot.annotation.springmvc.RequestParam;
import cn.zay.zayboot.mvc.MappingMethodDetail;
import cn.zay.zayboot.util.ObjectUtil;

import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * process @RequestParam annotation
 *
 * @author shuang.kou
 * @createTime 2020年09月27日 20:58:00
 **/
public class RequestParamParameterResolver implements ParameterResolver {
    @Override
    public Object resolve(Parameter parameter, Map<String,Object> map) {
        RequestParam requestParam = parameter.getDeclaredAnnotation(RequestParam.class);
        String requestParameter = requestParam.value();
        Object requestParameterValue = map.get(requestParameter);
        if (requestParameterValue == null) {
            throw new RuntimeException("未找到参数["+requestParameter+"], 无法注入");
        }
        return ObjectUtil.convert(parameter.getType(), requestParameterValue.toString());
    }
}
