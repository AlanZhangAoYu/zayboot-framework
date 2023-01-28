package cn.zay.zayboot.mvc.resolver;

import cn.zay.zayboot.annotation.springmvc.PathVariable;
import cn.zay.zayboot.mvc.MappingMethodDetail;
import cn.zay.zayboot.util.ObjectUtil;

import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * process @PathVariable annotation
 * @author ZAY
 */
public class PathVariableParameterResolver implements ParameterResolver {
    @Override
    public Object resolve(Parameter parameter, Map<String,Object> map) {
        PathVariable pathVariable = parameter.getDeclaredAnnotation(PathVariable.class);
        String requestParameter = pathVariable.value();
        Object requestParameterValue = map.get(requestParameter);
        if(requestParameterValue == null){
            throw new RuntimeException("未找到参数["+requestParameter+"], 无法注入");
        }
        return ObjectUtil.convert(parameter.getType(), requestParameterValue.toString());
    }
}
