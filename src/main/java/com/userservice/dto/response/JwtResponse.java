package com.userservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Jwt Response")
public class JwtResponse {

    @Schema(
            description = "Token received from server",
            example = "tywgubvbhhsj.ghgbjb.cghvbj"
    )
    private String token;
}
