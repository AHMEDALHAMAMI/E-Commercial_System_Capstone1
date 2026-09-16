package com.example.ecommerce_system.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantStock {

    @NotEmpty(message = "ID can not be empty")
    private String id;

    @NotEmpty(message = "Product ID can not be empty")
    private String productId;

    @NotEmpty(message = "Merchant ID can not be empty")
    private String merchantId;

    @NotNull(message = "Stock can not be empty")
    @Min(value = 11, message = "Stock must be more than 10 at start")
    private Integer stock;
}