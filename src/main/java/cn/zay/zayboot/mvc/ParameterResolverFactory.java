package cn.zay.zayboot.mvc;

import cn.zay.zayboot.annotation.springmvc.PathVariable;
import cn.zay.zayboot.annotation.springmvc.RequestBody;
import cn.zay.zayboot.annotation.springmvc.RequestParam;
import cn.zay.zayboot.mvc.resolver.ParameterResolver;
import cn.zay.zayboot.mvc.resolver.PathVariableParameterResolver;
import cn.zay.zayboot.mvc.resolver.RequestBodyParameterResolver;
import cn.zay.zayboot.mvc.resolver.RequestParamParameterResolver;
import java.lang.reflect.Parameter;

/**
 * @author ZAY
 */
public class ParameterResolverFactory {

    public static ParameterResolver get(Parameter parameter) {
        if (parameter.isAnnotationPresent(RequestParam.class)) {
            return new RequestParamParameterResolver();
        }
        if (parameter.isAnnotationPresent(PathVariable.class)) {
            return new PathVariableParameterResolver();
        }
        if (parameter.isAnnotationPresent(RequestBody.class)) {
            return new RequestBodyParameterResolver();
        }
        return null;
    }
}
