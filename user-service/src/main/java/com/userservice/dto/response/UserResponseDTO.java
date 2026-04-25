
package com.userservice.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Response for user details")
public class UserResponseDTO {

    @Schema(
            description = "User id",
            example = "1")
    private Integer userId;

    @Schema(
            description = "Full name of user",
            example = "John alice"
    )
    private String fullName;
}
