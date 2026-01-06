package com.digitalwallet.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorResponseDTO {
    private LocalDateTime timeStamp;
    private Integer errorCode;
    private String message;
    private Map<String,String> fieldErrors;
}
