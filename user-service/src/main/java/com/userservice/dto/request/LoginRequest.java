package com.userservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "User login request")
public class LoginRequest {


    @Schema(
            description = "Email",
            example="john@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Schema(
            description = "password",
            example="pasword#$@34321",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;
}
