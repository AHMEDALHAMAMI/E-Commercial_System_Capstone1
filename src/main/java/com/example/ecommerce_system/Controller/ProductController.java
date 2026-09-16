package com.example.ecommerce_system.Controller;

import com.example.ecommerce_system.Api.ApiResponse;
import com.example.ecommerce_system.Model.Product;
import com.example.ecommerce_system.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1. Add a new product
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(
            @Valid @RequestBody Product product,
            Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400)
                    .body(errors.getFieldError().getDefaultMessage());
        }

        productService.addProduct(product);

        return ResponseEntity.status(200)
                .body(new ApiResponse("Product added successfully"));
    }

    // 2. Get all products
    @GetMapping("/get")
    public ResponseEntity<?> getAllProducts() {
        ArrayList<Product> products = productService.getAllProducts();

        if (products.isEmpty()) {
            return ResponseEntity.status(404)
                    .body("No products found");
        }

        return ResponseEntity.status(200).body(products);
    }

    // 3. Get product by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        Product product = productService.getProductById(id);

        if (product == null) {
            return ResponseEntity.status(400)
                    .body("Product not found");
        }

        return ResponseEntity.status(200).body(product);
    }

    // 4. Update a product
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody Product product,
            Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400)
                    .body(errors.getFieldError().getDefaultMessage());
        }

        if (!product.getId().equals(id)) {
            return ResponseEntity.status(400)
                    .body("Product ID must match the path ID");
        }

        Product existingProduct = productService.getProductById(id);

        if (existingProduct == null) {
            return ResponseEntity.status(400)
                    .body("Product not found");
        }

        productService.updateProduct(id, product);

        return ResponseEntity.status(200)
                .body(new ApiResponse("Product updated successfully"));
    }

    // 5. Delete a product
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        Product product = productService.getProductById(id);

        if (product == null) {
            return ResponseEntity.status(400)
                    .body("Product not found");
        }

        productService.deleteProduct(id);

        return ResponseEntity.status(200)
                .body(new ApiResponse("Product deleted successfully"));
    }

    //===================== Extra 1 - 3 EndPoints ====================
    @GetMapping("/cheapest")
    public ResponseEntity<?> getCheapestProduct() {
        Product product = productService.getCheapestProduct();

        if (product == null) {
            return ResponseEntity.status(400).body("No products found");
        }

        return ResponseEntity.status(200).body(product);
    }
}