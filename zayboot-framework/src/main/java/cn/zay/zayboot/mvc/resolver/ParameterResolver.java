package cn.zay.zayboot.mvc.resolver;

import cn.zay.zayboot.mvc.MappingMethodDetail;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * @author ZAY
 */
public interface ParameterResolver {
    /**
     * Process method parameters
     * @param parameter    The parameter of the target method
     * @return Specific values corresponding to the parameters of the target method
     */
    Object resolve(Parameter parameter, Map<String,Object> map);
}
