
package com.example.ecommerce_system.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @NotEmpty(message = "Id must not be empty")
    private String id;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 4, message = "Name must be more than 3 characters")
    private String name;

    @NotNull(message = "Price must not be empty")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotEmpty(message = "Category Id must not be empty")
    private String categoryId;
}