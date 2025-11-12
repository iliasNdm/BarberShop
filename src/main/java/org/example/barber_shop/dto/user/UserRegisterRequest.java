package org.example.barber_shop.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email format")
    private String email;

    @NotBlank
    @Size(min = 2, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
