package com.digitalwallet.validation.validator;

import com.digitalwallet.validation.annotation.ValidUserAge;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserAgeValidator implements ConstraintValidator<ValidUserAge,Integer> {
    public boolean isValid(Integer age, ConstraintValidatorContext context) {
        if(age==null) return true;
        return age>=18;
    }
}
