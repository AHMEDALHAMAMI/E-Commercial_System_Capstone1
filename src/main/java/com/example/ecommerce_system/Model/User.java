
package com.example.ecommerce_system.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @NotEmpty(message = "Id must not be empty")
    private String id;

    @NotEmpty(message = "Username must not be empty")
    @Size(min = 6, message = "Username must be more than 5 characters")
    private String username;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 7, message = "Password must be more than 6 characters")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "Password must contain letters and digits"
    )
    private String password;

    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    private String email;

    @NotEmpty(message = "Role must not be empty")
    @Pattern(
            regexp = "^(Admin|Customer)$",
            message = "Role must be Admin or Customer"
    )
    private String role;

    @NotNull(message = "Balance must not be empty")
    @Positive(message = "Balance must be positive")
    private Double balance;
}