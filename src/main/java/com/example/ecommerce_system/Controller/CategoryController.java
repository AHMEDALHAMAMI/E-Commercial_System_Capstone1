package com.example.ecommerce_system.Controller;
import com.example.ecommerce_system.Api.ApiResponse;
import com.example.ecommerce_system.Model.Category;
import com.example.ecommerce_system.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 1. Get all categories
    @GetMapping("/get")
    public ResponseEntity<?> getCategories() {

        ArrayList<Category> categories = categoryService.getAllCategories();

        if (categories.isEmpty()) {
            return ResponseEntity.status(400).body("Categories not found");
        }

        return ResponseEntity.status(200).body(categories);
    }

    // 2. Add a new category
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(
            @RequestBody @Valid Category category,
            Errors errors) {

        // Validation error
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();

            return ResponseEntity.status(400).body(message);
        }

        categoryService.addCategory(category);

        return ResponseEntity.status(200)
                .body(new ApiResponse("Category added successfully"));
    }

    // 3. Get category by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable String id) {

        Category category = categoryService.getCategoryById(id);

        // Check category exists
        if (category == null) {
            return ResponseEntity.status(400).body("Category not found");
        }

        return ResponseEntity.status(200).body(category);
    }

    // 4. Update category
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable String id,
            @RequestBody @Valid Category category,
            Errors errors) {

        // Validation error
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();

            return ResponseEntity.status(400).body(message);
        }

        // Check category ID matches the path ID
        if (!category.getId().equals(id)) {
            return ResponseEntity.status(400)
                    .body("Category ID must match the path ID");
        }

        // Check category exists
        Category existingCategory = categoryService.getCategoryById(id);

        if (existingCategory == null) {
            return ResponseEntity.status(400).body("Category not found");
        }

        boolean isUpdated = categoryService.updateCategory(id, category);

        if (!isUpdated) {
            return ResponseEntity.status(400).body("Category update failed");
        }

        return ResponseEntity.status(200)
                .body(new ApiResponse("Category updated successfully"));
    }

    // 5. Delete category
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {

        // Check category exists
        Category category = categoryService.getCategoryById(id);

        if (category == null) {
            return ResponseEntity.status(400).body("Category not found");
        }

        boolean isDeleted = categoryService.deleteCategory(id);

        if (!isDeleted) {
            return ResponseEntity.status(400).body("Category deletion failed");
        }

        return ResponseEntity.status(200)
                .body(new ApiResponse("Category deleted successfully"));
    }
}