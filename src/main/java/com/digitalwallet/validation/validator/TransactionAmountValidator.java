package com.digitalwallet.validation.validator;

import com.digitalwallet.validation.annotation.ValidTransactionAmount;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class TransactionAmountValidator implements ConstraintValidator<ValidTransactionAmount, BigDecimal> {

private static final BigDecimal MAX_AMOUNT=new BigDecimal("10000.0");
    @Override
    public boolean isValid(BigDecimal amount, ConstraintValidatorContext constraintValidatorContext) {
        if(amount==null) return true;
        return amount.compareTo(BigDecimal.ZERO) >= 0 && amount.compareTo(MAX_AMOUNT)<=0;
    }
}
