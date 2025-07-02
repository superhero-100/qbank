package com.example.qbankapi.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class LoginBaseUserRequestDto {

    @NotBlank(message = "Username or email must not be blank")
    private String baseUserIdentifier;

    @NotBlank(message = "Password must not be blank")
//    @Pattern(
//            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
//            message = "Password must be at least 8 characters long and include uppercase, lowercase, digit, and special character"
//    )
    private String password;

    private String zoneId;

}
