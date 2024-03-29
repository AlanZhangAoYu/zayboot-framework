package cn.zay.zayboot.core.aop;

import cn.zay.zayboot.annotation.validation.Validated;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import javax.validation.*;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
/**
 * @author ZAY
 * Bean验证器
 */
public class BeanValidationInterceptor extends Interceptor {
    private final Validator validator;

    public BeanValidationInterceptor() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Override
    public boolean supports(Object bean) {
        return (bean != null && bean.getClass().isAnnotationPresent(Validated.class));
    }

    @Override
    public Object agent(MethodInvocation methodInvocation) {
        Annotation[][] parameterAnnotations = methodInvocation.getTargetMethod().getParameterAnnotations();
        Object[] args = methodInvocation.getArgs();
        for (int i = 0; i < args.length; i++) {
            boolean isNeedValidating = Arrays.stream(parameterAnnotations[i])
                    .anyMatch(annotation -> annotation.annotationType() == Valid.class);
            if (isNeedValidating) {
                Set<ConstraintViolation<Object>> results = validator.validate(args[i]);
                if (!results.isEmpty()) {
                    throw new ConstraintViolationException(results);
                }
            }
        }
        return methodInvocation.run();
    }
    @Override
    public Object agent(){
        return null;
    }
    @Override
    public Object agent(Object[] args){
        return null;
    }
}
