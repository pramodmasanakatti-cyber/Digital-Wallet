package com.digitalwallet.validation.annotation;

import com.digitalwallet.validation.validator.TransactionAmountValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TransactionAmountValidator.class)
@Target({ElementType.FIELD,ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTransactionAmount {
    String message() default "Transaction amount should be min=1 and max=10000";  // Default error message
    Class<?>[] groups() default {};    // For conditional validation
    Class<? extends Payload>[] payload() default {};

}
