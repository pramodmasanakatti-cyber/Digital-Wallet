package com.digitalwallet.validation.validator;

import com.digitalwallet.validation.annotation.ValidWalletBalance;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class WalletBalanceValidator implements ConstraintValidator <ValidWalletBalance, BigDecimal>{
    public boolean isValid(BigDecimal balance, ConstraintValidatorContext context) {
        if(balance==null) return true;
        return balance.compareTo(BigDecimal.ZERO)>=0;
    }
}
