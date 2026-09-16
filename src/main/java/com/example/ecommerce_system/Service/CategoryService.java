package com.example.ecommerce_system.Service;

import com.example.ecommerce_system.Model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryService {

    private ArrayList<Category> categories = new ArrayList<>();

    // Add a new category
    public void addCategory(Category category) {
        categories.add(category);
    }

    // Get all categories
    public ArrayList<Category> getAllCategories() {
        return categories;
    }

    // Get a category by ID
    public Category getCategoryById(String id) {
        for (Category category : categories) {
            if (category.getId().equals(id)) {
                return category;
            }
        }

        return null;
    }

    // Update a category
    public boolean updateCategory(String id, Category updatedCategory) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(id)) {
                categories.set(i, updatedCategory);
                return true;
            }
        }

        return false;
    }

    // Delete a category
    public boolean deleteCategory(String id) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(id)) {
                categories.remove(i);
                return true;
            }
        }

        return false;
    }
}