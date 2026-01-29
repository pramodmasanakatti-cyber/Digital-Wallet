package com.userservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Error response for structured error response")
public class ErrorResponseDTO {

    @Schema(
            description = "Time stamp",
            example = "2026-01-27T16:11:37.857911"
    )
    private LocalDateTime timeStamp;

    @Schema(
            description = "Http status code for error",
            example = "404"
    )
    private Integer errorCode;

    @Schema(
            description = "Error message",
            example = "User Not Found for userId:1"
    )
    private String message;

    @Schema(
            description = "Field errors",
            example = "email: email already exit"
    )
    private Map<String,String> fieldErrors;
}
