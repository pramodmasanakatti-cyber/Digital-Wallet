package com.digitalwallet.validation.annotation;

import com.digitalwallet.validation.validator.WalletBalanceValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import javax.print.DocFlavor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = WalletBalanceValidator.class)
@Target({ElementType.METHOD,ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidWalletBalance {
    String message() default "Wallet balance cannot be negative";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
