package com.digitalwallet.exception;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.digitalwallet.dto.response.ErrorResponseDTO;
import com.digitalwallet.exception.transaction.DuplicateExternalKeyException;
import com.digitalwallet.exception.transaction.InsufficientFundException;
import com.digitalwallet.exception.transaction.InvalidTransactionExceptionCustom;
import com.digitalwallet.exception.transaction.TransactionLimitExceedException;
import com.digitalwallet.exception.user.DuplicateEmailException;
import com.digitalwallet.exception.user.UserNotFoundException;
import com.digitalwallet.exception.wallet.WalletInactiveException;
import com.digitalwallet.exception.wallet.WalletNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.transaction.InvalidTransactionException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


     // Exception handling for UserNotFound,WalletNotFound
    @ExceptionHandler({UserNotFoundException.class, WalletNotFoundException.class})
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(RuntimeException exception) {
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                Map.of(exception instanceof UserNotFoundException?"User":"Wallet",exception.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
    }

    // Exception handling for DuplicateEmail
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateEmailException(RuntimeException exception) {
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                Map.of("email",exception.getMessage()));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
    }

    // Handles Bean Validation failures (@Valid)
    // Exception handling for MethodArgumentNotValid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String,String> fieldErrors=new HashMap<>();
                exception.getBindingResult()
                        .getFieldErrors()
                        .forEach(error->fieldErrors.put(error.getField(), error.getDefaultMessage()));

                ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Bean Validation failed",
                        fieldErrors);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }


    // Constraint violation Exception
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(ConstraintViolationException exception) {
        Map<String,String> fieldErrors=new HashMap<>();
        exception.getConstraintViolations().forEach(cv-> fieldErrors.put(cv.getPropertyPath().toString(),cv.getMessage()));
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bean Validation failed",
                fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    // InvalidFormatException (for Enum mismatched values)
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponseDTO> hanldeMethodArhumentTypeMismatchException(InvalidFormatException exception) {

        String fildName=exception.getPath().isEmpty()?"Unknown":exception.getPath().get(0).getFieldName();
        String allowedValues= Arrays.toString(exception.getTargetType().getEnumConstants());
        String message=String.format(
                "Invalid value '%s' for field '%s'. Allowed alues are: %s"
                ,exception.getValue()
                ,fildName
                ,allowedValues
        );
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                message,
                Map.of()
        );
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }


    // Exception handling for DuplicateExternalKey
    @ExceptionHandler(DuplicateExternalKeyException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateTransactionException(RuntimeException exception) {
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
    }


    // Exception handling for WalletInactive
    @ExceptionHandler(WalletInactiveException.class)
    public ResponseEntity<ErrorResponseDTO> handleWalletInactiveException(RuntimeException exception) {
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                exception.getMessage(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponseDTO);
    }

    // Exception handling for TransactionLimitExceed
    @ExceptionHandler(TransactionLimitExceedException.class)
    public ResponseEntity<ErrorResponseDTO> handleTransactionLimitException(Exception exception) {
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.TOO_MANY_REQUESTS.value(),
                exception.getMessage(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponseDTO);
    }

    // Exception handling for InvalidTransaction
    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidTransactionException(Exception exception) {
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

   // HTTP client error exception for microservices
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpClientErrorException(HttpClientErrorException exception) {
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                exception.getStatusCode().value(),
                exception.getMessage(),
                Map.of()
        );
        return ResponseEntity.status(exception.getStatusCode()).body(errorResponseDTO);
    }

    // Exception handling for insufficient funds
    @ExceptionHandler(InsufficientFundException.class)
    public ResponseEntity<ErrorResponseDTO> handleInsufficientFundException(Exception exception) {
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.PAYMENT_REQUIRED.value(),
                exception.getMessage(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(errorResponseDTO);
    }

    //Exception handling for InvalidTransactionExeptionCustom exception
    @ExceptionHandler(InvalidTransactionExceptionCustom.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidTransactionExceptionCustom(Exception exception) {
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

@ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceAccessException(ResourceAccessException exception) {
    ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
            LocalDateTime.now(),
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            exception.getMessage(),
            Map.of()
    );
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponseDTO);
}
    // Exception handling for other exceptions
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception exception) {
//        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
//                LocalDateTime.now(),
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                exception.getMessage(),
//                Map.of()
//        );
//    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDTO);
//    }
}
