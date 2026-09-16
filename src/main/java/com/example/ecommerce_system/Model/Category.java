
package com.example.ecommerce_system.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @NotEmpty(message = "Id must not be empty")
    private String id;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 4, message = "Name must be more than 3 characters")
    private String name;
}