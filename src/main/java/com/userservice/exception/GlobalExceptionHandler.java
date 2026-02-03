package com.userservice.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.userservice.dto.response.ErrorResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.ElementCollection;
import jakarta.transaction.InvalidTransactionException;
import jakarta.validation.ConstraintViolationException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.Signature;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({UserNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(RuntimeException exception) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                Map.of());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateEmailException(RuntimeException exception) {
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                Map.of("email",exception.getMessage()));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
    }

    @ExceptionHandler(AuthenticationException.class)
    public  ResponseEntity<ErrorResponseDTO> handleAuthenticationException(Exception exception) {
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                exception.getMessage(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponseDTO);
    }

    // Handles Bean Validation failures (@Valid)
   //  Exception handling for MethodArgumentNotValid
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

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponseDTO> handleExpiredJwtEception(ExpiredJwtException exception) {
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
               "Token is expired",
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDTO);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponseDTO> handleSignatureEception(SignatureException exception) {
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid signature",
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDTO);
    }
//    @ExceptionHandler(SignatureException.class)
//    public ResponseEntity<ErrorResponseDTO> handleSignature

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
