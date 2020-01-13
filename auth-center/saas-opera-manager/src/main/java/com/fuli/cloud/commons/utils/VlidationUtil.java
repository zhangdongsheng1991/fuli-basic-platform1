package com.fuli.cloud.commons.utils;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import javax.validation.*;
import java.util.Set;


/**
 * <pre>
 * Description:
 * </pre>
 *
 * @author chenyi
 * @date 9:54 2019/8/1
 **/
public abstract class VlidationUtil {

    private static Validator validator;

    static {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.getValidator();
    }


    /**
     * <pre>
     * Description: 校验javabean字段
     * </pre>
     *
     * @author chenyi
     * @date 9:55 2019/8/1
     **/
    public static <T> void validate(T t) throws ValidationException {
        Set<ConstraintViolation<T>> set = validator.validate(t);
        if (set.size() > 0) {
            Set<String> validateError = Sets.newLinkedHashSet();
            for (ConstraintViolation<T> val : set) {
                validateError.add(val.getMessage());
            }
            throw new ValidationException(StringUtils.join(validateError, "；"));
        }
    }

    /**
     * <pre>
     * Description: 校验javabean字段
     * </pre>
     *
     * @author chenyi
     * @date 9:55 2019/8/1
     **/
    public static <T> Set<String> validateRetureSet(T t) {
        Set<ConstraintViolation<T>> set = validator.validate(t);
        if (set.size() > 0) {
            Set<String> validateError = Sets.newLinkedHashSet();
            for (ConstraintViolation<T> val : set) {
                validateError.add(val.getMessage());
            }
            return validateError;
        }
        return null;
    }

}
