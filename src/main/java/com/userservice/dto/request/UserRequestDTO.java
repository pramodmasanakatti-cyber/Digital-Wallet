package com.userservice.dto.request;

import com.userservice.entity.Role;
import com.userservice.validation.annotation.ValidUserAge;
import com.userservice.validation.groups.Create;
import com.userservice.validation.groups.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "User creation request")
public class UserRequestDTO {
    @NotNull(groups = Update.class)
    @Schema(requiredMode = Schema.RequiredMode.AUTO)
    private Integer userId;


    @Schema(
            description = "Full name",
            example = "John alice"
    )
    @NotNull(groups = {Create.class, Update.class})
    private String fullName;


    @Email(message = "Invalid email format")
    @NotBlank(message = "Email should not be blank",groups = {Create.class, Update.class})
    @Schema(
            description = "Email address",
            example = "john@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;


    @Schema(
            description = "Phone number",
            example = "1234567890",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(groups = Create.class)
    private String phone;

    @NotBlank
    @Schema(
            description = "Password",
            example = "passqwor#@422",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;

    @NotNull(groups = Create.class)
    @ValidUserAge
    @Schema(
            description = "Age",
            example = "20",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private  Integer age;

    @NotNull
    @Schema(
            description = "Role",
            example = "USER",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Role role;
}
