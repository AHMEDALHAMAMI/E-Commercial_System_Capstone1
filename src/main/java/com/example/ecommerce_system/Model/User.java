package com.example.ecommerce_system.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    @NotBlank(message = "ID cannot be empty")
    private String id;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 4, message = "Username must be at least 4 characters")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Role cannot be empty")
    @Pattern(regexp = "Customer|Admin", message = "Role must be Customer or Admin")
    private String role;

    @PositiveOrZero(message = "Balance cannot be negative")
    private double balance;

    private int purchaseCount = 0;

    private String referredBy;

    public User() {
    }
}
