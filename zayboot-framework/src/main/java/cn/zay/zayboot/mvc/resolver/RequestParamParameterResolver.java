package cn.zay.zayboot.mvc.resolver;

import cn.zay.zayboot.annotation.springmvc.RequestParam;
import cn.zay.zayboot.mvc.MappingMethodDetail;
import cn.zay.zayboot.util.ObjectUtil;

import java.lang.reflect.Parameter;

/**
 * process @RequestParam annotation
 *
 * @author shuang.kou
 * @createTime 2020年09月27日 20:58:00
 **/
public class RequestParamParameterResolver implements ParameterResolver {
    @Override
    public Object resolve(MappingMethodDetail methodDetail, Parameter parameter) {
        RequestParam requestParam = parameter.getDeclaredAnnotation(RequestParam.class);
        String requestParameter = requestParam.value();
        String requestParameterValue = (String) methodDetail.getQueryParameterMap().get(requestParameter);
        if (requestParameterValue == null) {
            throw new IllegalArgumentException("The specified parameter " + requestParameter + " can not be null!");
        }
        // convert the parameter to the specified type
        return ObjectUtil.convert(parameter.getType(), requestParameterValue);

    }
}
