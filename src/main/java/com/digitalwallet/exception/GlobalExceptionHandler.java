package com.digitalwallet.exception;

import com.digitalwallet.dto.response.ErrorResponseDTO;
import jakarta.transaction.InvalidTransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


     // Exception handling for UserNotFound,WalletNotFound
    @ExceptionHandler({UserNotFoundException.class,WalletNotFoundException.class})
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

    // Exception handling for MethodArgumentNotValid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String,String> fieldrrors=new HashMap<>();
                exception.getBindingResult()
                        .getFieldErrors()
                        .forEach(error->fieldrrors.put(error.getField(), error.getDefaultMessage()));
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                exception.getFieldError().getDefaultMessage(),
                        fieldrrors);
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

    // Exception handling for other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception exception) {
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                Map.of()
        );
return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDTO);
    }
}
