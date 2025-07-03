package com.example.qbankapi.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class RegisterBaseUserRequestDto {

    @NotBlank(message = "Username must not be blank")
    @Pattern(regexp = "^[A-Za-z0-9_.]{3,20}$", message = "Username must be alphanumeric and 3-20 characters long.")
    private String username;

    @NotBlank(message = "Email must not be blank")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password must not be blank")
//    @Pattern(
//            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
//            message = "Password must be at least 8 characters long and include uppercase, lowercase, digit, and special character"
//    )
    private String password;

    @NotBlank(message = "Confirm Password must not be blank")
    private String confirmPassword;

    private String zoneId;

    @NotNull(message = "Role must not be null")
    private Role role;

    public enum Role {
        INSTRUCTOR, PARTICIPANT
    }

}
