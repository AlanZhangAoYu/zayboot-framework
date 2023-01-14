package cn.zay.zayboot.mvc.resolver;

import cn.zay.zayboot.mvc.MappingMethodDetail;

import java.lang.reflect.Parameter;

/**
 * @author ZAY
 */
public interface ParameterResolver {
    /**
     * Process method parameters
     *
     * @param methodDetail Target method related information
     * @param parameter    The parameter of the target method
     * @return Specific values corresponding to the parameters of the target method
     */
    Object resolve(MappingMethodDetail methodDetail, Parameter parameter);
}
