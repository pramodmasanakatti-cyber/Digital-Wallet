package com.digitalwallet.exception;

import com.digitalwallet.dto.reqsponse.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class,WalletNotFoundException.class})
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(RuntimeException exception) {
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO();
        errorResponseDTO.setMessage(exception.getMessage());
        errorResponseDTO.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorResponseDTO.setTimeStamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateEmailException(RuntimeException exception) {
        ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO();
        errorResponseDTO.setTimeStamp(LocalDateTime.now());
        errorResponseDTO.setMessage(exception.getMessage());
        errorResponseDTO.setStatusCode(HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
    }
}
