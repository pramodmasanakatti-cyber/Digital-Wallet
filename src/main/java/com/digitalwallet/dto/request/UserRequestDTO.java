package com.digitalwallet.dto.request;

import com.digitalwallet.validation.annotation.ValidUserAge;
import com.digitalwallet.validation.groups.Create;
import com.digitalwallet.validation.groups.Update;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequestDTO {
    @NotNull(groups = Update.class)
    private Integer userId;


    @NotNull(groups = {Create.class, Update.class})
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email should not be blank",groups = {Create.class, Update.class})
    private String email;

    @NotNull(groups = Create.class)
    private String phone;

    @NotNull(groups = Create.class)
    @ValidUserAge
    private  Integer age;
}
