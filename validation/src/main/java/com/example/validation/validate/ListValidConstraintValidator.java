package com.example.validation.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;

/**
 *  自定义校验器
 */
public class ListValidConstraintValidator implements ConstraintValidator<ListValue, Integer> {
    HashSet<Integer> set = new HashSet();
    // 校验器, 初始化方法
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] values = constraintAnnotation.values();
        for (Integer val: values) {
            set.add(val);
        }
    }
    // 校验器, 校验方法
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(integer);
    }
}
