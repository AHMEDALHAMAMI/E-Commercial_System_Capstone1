package com.example.ecommerce_system.Service;

import com.example.ecommerce_system.Model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProductService {

    private ArrayList<Product> products = new ArrayList<>();

    // Add a new product
    public void addProduct(Product product) {
        products.add(product);
    }

    // Get all products
    public ArrayList<Product> getAllProducts() {
        return products;
    }

    // Get product by ID
    public Product getProductById(String id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    // Update a product
    public boolean updateProduct(String id, Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                products.set(i, updatedProduct);
                return true;
            }
        }
        return false;
    }

    // Delete a product
    public boolean deleteProduct(String id) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                products.remove(i);
                return true;
            }
        }
        return false;
    }


    //==================== Extra 1 -3 EndPoints ====================
    public Product getCheapestProduct() {
        if (products.isEmpty()) {
            return null;
        }

        Product cheapestProduct = products.get(0);

        for (Product product : products) {
            if (product.getPrice() < cheapestProduct.getPrice()) {
                cheapestProduct = product;
            }
        }

        return cheapestProduct;
    }
}