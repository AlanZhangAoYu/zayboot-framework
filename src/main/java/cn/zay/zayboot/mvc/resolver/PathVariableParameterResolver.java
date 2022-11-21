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
    public Object resolve(MappingMethodDetail methodDetail, Parameter parameter) {
        PathVariable pathVariable = parameter.getDeclaredAnnotation(PathVariable.class);
        String requestParameter = pathVariable.value();
        Map<String, String> urlParameterMappings = methodDetail.getUrlParameterMappings();
        String requestParameterValue = urlParameterMappings.get(requestParameter);
        return ObjectUtil.convert(parameter.getType(), requestParameterValue);
    }
}
