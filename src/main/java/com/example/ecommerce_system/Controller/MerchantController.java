package com.example.ecommerce_system.Controller;

import com.example.ecommerce_system.Api.ApiResponse;
import com.example.ecommerce_system.Model.Merchant;
import com.example.ecommerce_system.Service.MerchantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/merchant")
@AllArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    // 1. Get all merchants
    @GetMapping("/get")
    public ResponseEntity<?> getMerchants() {

        ArrayList<Merchant> merchants =
                merchantService.getAllMerchants();

        if (merchants.isEmpty()) {
            return ResponseEntity.status(400)
                    .body("Merchant not found");
        }

        return ResponseEntity.status(200).body(merchants);
    }

    // 2. Add a new merchant
    @PostMapping("/add")
    public ResponseEntity<?> addMerchant(@RequestBody @Valid Merchant merchant,
            Errors errors) {

        // Validation error
        if (errors.hasErrors()) {
            String message =
                    errors.getFieldError().getDefaultMessage();

            return ResponseEntity.status(400).body(message);
        }

        merchantService.addMerchant(merchant);

        return ResponseEntity.status(200)
                .body(new ApiResponse("Merchant added successfully"));
    }

    // 3. Get merchant by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getMerchantById(@PathVariable String id) {

        Merchant merchant =
                merchantService.getMerchantById(id);

        // Check merchant exists
        if (merchant == null) {
            return ResponseEntity.status(400)
                    .body("Merchant not found");
        }

        return ResponseEntity.status(200).body(merchant);
    }

    // 4. Update merchant
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMerchant(@PathVariable String id, @RequestBody @Valid Merchant merchant,
            Errors errors) {

        // Validation error
        if (errors.hasErrors()) {
            String message =
                    errors.getFieldError().getDefaultMessage();

            return ResponseEntity.status(400).body(message);
        }

        // Check merchant ID matches the path ID
        if (!merchant.getId().equals(id)) {
            return ResponseEntity.status(400)
                    .body("Merchant ID must match the path ID");
        }

        // Check merchant exists
        Merchant existingMerchant =
                merchantService.getMerchantById(id);

        if (existingMerchant == null) {
            return ResponseEntity.status(400)
                    .body("Merchant not found");
        }

        boolean isUpdated =
                merchantService.updateMerchant(id, merchant);

        if (!isUpdated) {
            return ResponseEntity.status(400)
                    .body("Merchant could not be updated");
        }

        return ResponseEntity.status(200)
                .body(new ApiResponse("Merchant updated successfully"));
    }

    // 5. Delete merchant
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMerchant(@PathVariable String id) {

        // Check merchant exists
        Merchant merchant =
                merchantService.getMerchantById(id);

        if (merchant == null) {
            return ResponseEntity.status(400)
                    .body("Merchant not found");
        }

        boolean isDeleted =
                merchantService.deleteMerchant(id);

        if (!isDeleted) {
            return ResponseEntity.status(400)
                    .body("Merchant could not be deleted");
        }

        return ResponseEntity.status(200)
                .body(new ApiResponse("Merchant deleted successfully"));
    }
}