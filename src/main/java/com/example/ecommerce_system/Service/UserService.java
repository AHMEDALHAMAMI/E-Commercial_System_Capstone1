package com.example.ecommerce_system.Service;

import com.example.ecommerce_system.Model.MerchantStock;
import com.example.ecommerce_system.Model.Product;
import com.example.ecommerce_system.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    private ArrayList<User> users = new ArrayList<>();

    // Add a new user
    public void addUser(User user) {
        users.add(user);
    }

    // Get all users
    public ArrayList<User> getAllUsers() {
        return users;
    }

    // Get user by ID
    public User getUserById(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    // Update user
    public boolean updateUser(String id, User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                users.set(i, updatedUser);
                return true;
            }
        }
        return false;
    }

    // Delete user
    public boolean deleteUser(String id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                users.remove(i);
                return true;
            }
        }
        return false;
    }

    // Buy a normal product
    public double buyProduct(User user, Product product, MerchantStock merchantStock) {

        double finalPrice = product.getPrice();

        merchantStock.setStock(merchantStock.getStock() - 1);
        user.setBalance(user.getBalance() - finalPrice);

        return finalPrice;
    }

    //================= Extra endpoints ===================

    // Buy a product during Saudi National Day with a 96% discount
    public double buySaudiNationalDay(User user, Product product, MerchantStock merchantStock) {

        double finalPrice = product.getPrice() * 0.04;

        merchantStock.setStock(merchantStock.getStock() - 1);
        user.setBalance(user.getBalance() - finalPrice);

        return finalPrice;
    }

    // Buy multiple products with a quantity-based discount
    public double buyBulkProducts(User user, Product product, MerchantStock merchantStock,
            int quantity) {

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

        merchantStock.setStock(merchantStock.getStock() - quantity);
        user.setBalance(user.getBalance() - finalPrice);

        return finalPrice;
    }

    // Buy a product with a 10% loyalty discount and free delivery
    public double buyLoyaltyProduct(User user, Product product, MerchantStock merchantStock) {

        double finalPrice = product.getPrice() * 0.90;

        merchantStock.setStock(merchantStock.getStock() - 1);
        user.setBalance(user.getBalance() - finalPrice);

        return finalPrice;
    }

    // Buy a product with a 30% referral discount
    public double buyReferralProduct(User user, Product product, MerchantStock merchantStock) {

        double finalPrice = product.getPrice() * 0.70;

        merchantStock.setStock(merchantStock.getStock() - 1);
        user.setBalance(user.getBalance() - finalPrice);

        return finalPrice;
    }

    // ============== Extra 1- 3 EndPoints ============
    // Add balance to a user
    public boolean addBalance(String userId, double amount) {

        User user = getUserById(userId);

        if (user == null) {
            return false;
        }

        user.setBalance(user.getBalance() + amount);

        return true;
    }
}