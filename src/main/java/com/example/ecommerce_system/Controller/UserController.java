package com.example.ecommerce_system.Controller;

import com.example.ecommerce_system.Api.ApiResponse;
import com.example.ecommerce_system.Model.MerchantStock;
import com.example.ecommerce_system.Model.Product;
import com.example.ecommerce_system.Model.User;
import com.example.ecommerce_system.Service.MerchantService;
import com.example.ecommerce_system.Service.MerchantStockService;
import com.example.ecommerce_system.Service.ProductService;
import com.example.ecommerce_system.Service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProductService productService;
    private final MerchantService merchantService;
    private final MerchantStockService merchantStockService;

    // 1. Get all users
    @GetMapping("/get")
    public ResponseEntity<?> getUsers() {
        ArrayList<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            return ResponseEntity.status(404).body("User not found!");
        }

        return ResponseEntity.status(200).body(users);
    }

    // 2. Add a new user
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }

        userService.addUser(user);

        return ResponseEntity.status(200)
                .body(new ApiResponse("User added successfully"));
    }

    // 3. Get user by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.status(400).body("User not found");
        }

        return ResponseEntity.status(200).body(user);
    }

    // 4. Update user
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }

        if (!user.getId().equals(id)) {
            return ResponseEntity.status(400).body("User ID must match the path ID");
        }

        User existingUser = userService.getUserById(id);

        if (existingUser == null) {
            return ResponseEntity.status(400).body("User not found");
        }

        boolean isUpdated = userService.updateUser(id, user);

        if (!isUpdated) {
            return ResponseEntity.status(400).body("User update failed");
        }

        return ResponseEntity.status(200)
                .body(new ApiResponse("User updated successfully"));
    }

    // 5. Delete user
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.status(400).body("User not found");
        }

        boolean isDeleted = userService.deleteUser(id);

        if (!isDeleted) {
            return ResponseEntity.status(400).body("User deletion failed");
        }

        return ResponseEntity.status(200)
                .body(new ApiResponse("User deleted successfully"));
    }

    // 6. Buy a normal product
    @PostMapping("/buy/{userId}/{productId}/{merchantId}")
    public ResponseEntity<?> buyProduct(@PathVariable String userId, @PathVariable String productId, @PathVariable String merchantId) {
        User user = userService.getUserById(userId);

        if (user == null) {
            return ResponseEntity.status(400).body("User not found");
        }

        Product product = productService.getProductById(productId);

        if (product == null) {
            return ResponseEntity.status(400).body("Product not found");
        }

        if (merchantService.getMerchantById(merchantId) == null) {
            return ResponseEntity.status(400).body("Merchant not found");
        }

        MerchantStock merchantStock = merchantStockService.getMerchantStockByProductAndMerchant(productId, merchantId);

        if (merchantStock == null) {
            return ResponseEntity.status(400)
                    .body("Merchant does not have this product in stock");
        }

        if (merchantStock.getStock() <= 0) {
            return ResponseEntity.status(400).body("Product is out of stock");
        }

        if (user.getBalance() < product.getPrice()) {
            return ResponseEntity.status(400).body("Insufficient balance");
        }

        double finalPrice = userService.buyProduct(user, product, merchantStock);
        String formattedPrice = String.format("%.2f", finalPrice);

        return ResponseEntity.status(200)
                .body(new ApiResponse("Product purchased successfully. Final price: " + formattedPrice + " SAR"));
    }

    //================= Extra endpoints ===================

    // 7. Saudi National Day purchase with a 96% discount
    @PostMapping("/buy/saudi-national-day/{userId}/{productId}/{merchantId}")
    public ResponseEntity<?> buySaudiNationalDay(@PathVariable String userId, @PathVariable String productId, @PathVariable String merchantId) {
        User user = userService.getUserById(userId);

        if (user == null) {
            return ResponseEntity.status(400).body("User not found");
        }

        Product product = productService.getProductById(productId);

        if (product == null) {
            return ResponseEntity.status(400).body("Product not found");
        }

        if (merchantService.getMerchantById(merchantId) == null) {
            return ResponseEntity.status(400).body("Merchant not found");
        }

        MerchantStock merchantStock = merchantStockService.getMerchantStockByProductAndMerchant(productId, merchantId);

        if (merchantStock == null) {
            return ResponseEntity.status(400)
                    .body("Merchant does not have this product in stock");
        }

        if (merchantStock.getStock() <= 0) {
            return ResponseEntity.status(400).body("Product is out of stock");
        }

        double finalPrice = product.getPrice() * 0.04;

        if (user.getBalance() < finalPrice) {
            return ResponseEntity.status(400).body("Insufficient balance");
        }

        finalPrice = userService.buySaudiNationalDay(user, product, merchantStock);
        String formattedPrice = String.format("%.2f", finalPrice);

        return ResponseEntity.status(200)
                .body(new ApiResponse("Saudi National Day purchase completed with a 96% discount. Final price: " + formattedPrice + " SAR"));
    }

    // 8. Bulk purchase
    @PostMapping("/buy/bulk/{userId}/{productId}/{merchantId}/{quantity}")
    public ResponseEntity<?> buyBulkProducts(@PathVariable String userId, @PathVariable String productId, @PathVariable String merchantId, @PathVariable int quantity) {
        User user = userService.getUserById(userId);

        if (user == null) {
            return ResponseEntity.status(400).body("User not found");
        }

        Product product = productService.getProductById(productId);

        if (product == null) {
            return ResponseEntity.status(400).body("Product not found");
        }

        if (merchantService.getMerchantById(merchantId) == null) {
            return ResponseEntity.status(400).body("Merchant not found");
        }

        if (quantity <= 0) {
            return ResponseEntity.status(400).body("Quantity must be positive");
        }

        MerchantStock merchantStock = merchantStockService.getMerchantStockByProductAndMerchant(productId, merchantId);

        if (merchantStock == null) {
            return ResponseEntity.status(400)
                    .body("Merchant does not have this product in stock");
        }

        if (merchantStock.getStock() < quantity) {
            return ResponseEntity.status(400).body("Not enough stock available");
        }

        double discountRate = 0;

        if (quantity >= 10) {
            discountRate = 0.20;
        } else if (quantity >= 5) {
            discountRate = 0.10;
        } else if (quantity >= 3) {
            discountRate = 0.05;
        }

        double totalPrice = product.getPrice() * quantity;
        double finalPrice = totalPrice * (1 - discountRate);

        if (user.getBalance() < finalPrice) {
            return ResponseEntity.status(400).body("Insufficient balance");
        }

        finalPrice = userService.buyBulkProducts(user, product, merchantStock, quantity);
        String formattedPrice = String.format("%.2f", finalPrice);

        return ResponseEntity.status(200)
                .body(new ApiResponse("Bulk purchase completed successfully. Final price: " + formattedPrice + " SAR"));
    }

    // 9. Loyalty purchase with a 10% discount and free delivery
    @PostMapping("/buy/loyalty/{userId}/{productId}/{merchantId}")
    public ResponseEntity<?> buyWithLoyalty(@PathVariable String userId, @PathVariable String productId, @PathVariable String merchantId) {
        User user = userService.getUserById(userId);

        if (user == null) {
            return ResponseEntity.status(400).body("User not found");
        }

        Product product = productService.getProductById(productId);

        if (product == null) {
            return ResponseEntity.status(400).body("Product not found");
        }

        if (merchantService.getMerchantById(merchantId) == null) {
            return ResponseEntity.status(400).body("Merchant not found");
        }

        MerchantStock merchantStock = merchantStockService.getMerchantStockByProductAndMerchant(productId, merchantId);

        if (merchantStock == null) {
            return ResponseEntity.status(400)
                    .body("Merchant does not have this product in stock");
        }

        if (merchantStock.getStock() <= 0) {
            return ResponseEntity.status(400).body("Product is out of stock");
        }

        double finalPrice = product.getPrice() * 0.90;

        if (user.getBalance() < finalPrice) {
            return ResponseEntity.status(400).body("Insufficient balance");
        }

        finalPrice = userService.buyLoyaltyProduct(user, product, merchantStock);
        String formattedPrice = String.format("%.2f", finalPrice);

        return ResponseEntity.status(200)
                .body(new ApiResponse("Loyalty purchase completed with a 10% discount and free delivery. Final price: " + formattedPrice + " SAR"));
    }

    // 10. Referral purchase with a 30% discount
    @PostMapping("/buy/referral/{userId}/{productId}/{merchantId}")
    public ResponseEntity<?> buyWithReferral(@PathVariable String userId, @PathVariable String productId, @PathVariable String merchantId) {
        User user = userService.getUserById(userId);

        if (user == null) {
            return ResponseEntity.status(400).body("User not found");
        }

        Product product = productService.getProductById(productId);

        if (product == null) {
            return ResponseEntity.status(400).body("Product not found");
        }

        if (merchantService.getMerchantById(merchantId) == null) {
            return ResponseEntity.status(400).body("Merchant not found");
        }

        MerchantStock merchantStock = merchantStockService.getMerchantStockByProductAndMerchant(productId, merchantId);

        if (merchantStock == null) {
            return ResponseEntity.status(400)
                    .body("Merchant does not have this product in stock");
        }

        if (merchantStock.getStock() <= 0) {
            return ResponseEntity.status(400).body("Product is out of stock");
        }

        double finalPrice = product.getPrice() * 0.70;

        if (user.getBalance() < finalPrice) {
            return ResponseEntity.status(400).body("Insufficient balance");
        }

        finalPrice = userService.buyReferralProduct(user, product, merchantStock);
        String formattedPrice = String.format("%.2f", finalPrice);

        return ResponseEntity.status(200)
                .body(new ApiResponse("Referral purchase completed with a 30% discount. Final price: " + formattedPrice + " SAR"));
    }


    // =============== Extra 1 - 3 EndPoints =================
    // 11. Add balance to a user
    @PutMapping("/add-balance/{userId}/{amount}")
    public ResponseEntity<?> addBalance(@PathVariable String userId, @PathVariable double amount) {
        if (amount <= 0) {
            return ResponseEntity.status(400).body("Amount must be greater than 0");
        }

        User user = userService.getUserById(userId);

        if (user == null) {
            return ResponseEntity.status(400).body("User not found");
        }

        boolean isAdded = userService.addBalance(userId, amount);

        if (!isAdded) {
            return ResponseEntity.status(400).body("Balance addition failed");
        }

        return ResponseEntity.status(200)
                .body(new ApiResponse("Balance added successfully"));
    }
}