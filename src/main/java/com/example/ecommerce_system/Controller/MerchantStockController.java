package com.example.ecommerce_system.Controller;

import com.example.ecommerce_system.Api.ApiResponse;
import com.example.ecommerce_system.Model.MerchantStock;
import com.example.ecommerce_system.Service.MerchantService;
import com.example.ecommerce_system.Service.MerchantStockService;
import com.example.ecommerce_system.Service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/merchant-stock")
@AllArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;
    private final ProductService productService;
    private final MerchantService merchantService;

    // 1. Get all merchant stocks
    @GetMapping("/get")
    public ResponseEntity<?> getMerchantStocks() {

        ArrayList<MerchantStock> merchantStocks =
                merchantStockService.getAllMerchantStocks();

        if (merchantStocks.isEmpty()) {
            return ResponseEntity.status(400)
                    .body("Merchant stock not found");
        }

        return ResponseEntity.status(200).body(merchantStocks);
    }

    // 2. Add a new merchant stock
    @PostMapping("/add")
    public ResponseEntity<?> addMerchantStock(
            @RequestBody @Valid MerchantStock merchantStock,
            Errors errors) {

        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }

        merchantStockService.addMerchantStock(merchantStock);

        return ResponseEntity.status(200)
                .body(new ApiResponse("Merchant stock added successfully"));
    }

    // 3. Get merchant stock by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getMerchantStockById(@PathVariable String id) {

        MerchantStock merchantStock =
                merchantStockService.getMerchantStockById(id);

        if (merchantStock == null) {
            return ResponseEntity.status(400)
                    .body("Merchant stock not found");
        }

        return ResponseEntity.status(200).body(merchantStock);
    }

    // 4. Add additional stock
    @PutMapping("/add-stock/{productId}/{merchantId}/{additionalStock}")
    public ResponseEntity<?> addAdditionalStock(
            @PathVariable String productId,
            @PathVariable String merchantId,
            @PathVariable int additionalStock) {

        // Check that the product exists
        if (productService.getProductById(productId) == null) {
            return ResponseEntity.status(400)
                    .body("Product not found");
        }

        // Check that the merchant exists
        if (merchantService.getMerchantById(merchantId) == null) {
            return ResponseEntity.status(400)
                    .body("Merchant not found");
        }

        // Check that the additional stock is positive
        if (additionalStock <= 0) {
            return ResponseEntity.status(400)
                    .body("Additional stock must be positive");
        }

        // Check that the merchant has this product
        MerchantStock merchantStock =
                merchantStockService.getMerchantStockByProductAndMerchant(
                        productId,
                        merchantId);

        if (merchantStock == null) {
            return ResponseEntity.status(400)
                    .body("Merchant does not have this product in stock");
        }

        boolean isAdded = merchantStockService.addAdditionalStock(
                productId,
                merchantId,
                additionalStock);

        if (!isAdded) {
            return ResponseEntity.status(400)
                    .body("Additional stock could not be added");
        }

        return ResponseEntity.status(200)
                .body(new ApiResponse("Additional stock added successfully"));
    }

    //================= Extra end points ===================

    // 5. Filter merchant stocks by quantity
    @GetMapping("/filter/{stock}")
    public ResponseEntity<?> getMerchantStocksByStockGreaterThan(
            @PathVariable int stock) {

        if (stock < 0) {
            return ResponseEntity.status(400)
                    .body("Stock cannot be negative");
        }

        ArrayList<MerchantStock> filteredMerchantStocks =
                merchantStockService.getMerchantStocksByStockGreaterThan(stock);

        if (filteredMerchantStocks.isEmpty()) {
            return ResponseEntity.status(400)
                    .body("No merchant stocks found with stock greater than " + stock);
        }

        return ResponseEntity.status(200).body(filteredMerchantStocks);
    }

    // 6. Update merchant stock
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMerchantStock(
            @PathVariable String id,
            @RequestBody @Valid MerchantStock merchantStock,
            Errors errors) {

        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }

        if (!merchantStock.getId().equals(id)) {
            return ResponseEntity.status(400)
                    .body("Merchant stock ID must match the path ID");
        }

        MerchantStock existingMerchantStock =
                merchantStockService.getMerchantStockById(id);

        if (existingMerchantStock == null) {
            return ResponseEntity.status(400)
                    .body("Merchant stock not found");
        }

        boolean isUpdated =
                merchantStockService.updateMerchantStock(id, merchantStock);

        if (!isUpdated) {
            return ResponseEntity.status(400)
                    .body("Merchant stock could not be updated");
        }

        return ResponseEntity.status(200)
                .body(new ApiResponse("Merchant stock updated successfully"));
    }

    // 7. Delete merchant stock
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMerchantStock(@PathVariable String id) {

        MerchantStock merchantStock =
                merchantStockService.getMerchantStockById(id);

        if (merchantStock == null) {
            return ResponseEntity.status(400)
                    .body("Merchant stock not found");
        }

        boolean isDeleted = merchantStockService.deleteMerchantStock(id);

        if (!isDeleted) {
            return ResponseEntity.status(400)
                    .body("Merchant stock could not be deleted");
        }

        return ResponseEntity.status(200)
                .body(new ApiResponse("Merchant stock deleted successfully"));
    }
}