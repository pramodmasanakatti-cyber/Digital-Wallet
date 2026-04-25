package com.userservice.validation.annotation;

import com.userservice.validation.validator.UserAgeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserAgeValidator.class)
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserAge {
    String message() default "User age must be atleast 18 years old";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};
}
