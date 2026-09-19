package com.example.ecommerce_system.Service;

import com.example.ecommerce_system.Api.ApiResponse;
import com.example.ecommerce_system.Model.MerchantStock;
import com.example.ecommerce_system.Model.Product;
import com.example.ecommerce_system.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    private final ArrayList<User> users = new ArrayList<>();

    public ArrayList<User> getAllUsers() {
        return users;
    }

    public ApiResponse addUser(User user) {
        users.add(user);
        return new ApiResponse("User added successfully");
    }

    public User getUserById(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public ApiResponse updateUser(String id, User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {

                User existingUser = users.get(i);

                user.setPurchaseCount(existingUser.getPurchaseCount());
                user.setReferredBy(existingUser.getReferredBy());

                users.set(i, user);

                return new ApiResponse("User updated successfully");
            }
        }

        return new ApiResponse("User not found");
    }

    public ApiResponse deleteUser(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                users.remove(user);
                return new ApiResponse("User deleted successfully");
            }
        }

        return new ApiResponse("User not found");
    }

    public double buyProduct(
            User user,
            Product product,
            MerchantStock merchantStock) {

        double finalPrice = product.getPrice();

        user.setBalance(user.getBalance() - finalPrice);
        merchantStock.setStock(merchantStock.getStock() - 1);
        user.setPurchaseCount(user.getPurchaseCount() + 1);

        return finalPrice;
    }

    public double buySaudiNationalDay(
            User user,
            Product product,
            MerchantStock merchantStock) {

        double finalPrice = product.getPrice() * 0.04;

        user.setBalance(user.getBalance() - finalPrice);
        merchantStock.setStock(merchantStock.getStock() - 1);
        user.setPurchaseCount(user.getPurchaseCount() + 1);

        return finalPrice;
    }

    public double calculateBulkPrice(Product product, int quantity) {

        double discountRate = 0;

        if (quantity >= 10) {
            discountRate = 0.20;
        } else if (quantity >= 5) {
            discountRate = 0.10;
        } else if (quantity >= 3) {
            discountRate = 0.05;
        }

        double totalPrice = product.getPrice() * quantity;

        return totalPrice * (1 - discountRate);
    }

    public double buyBulkProducts(
            User user,
            Product product,
            MerchantStock merchantStock,
            int quantity) {

        double finalPrice = calculateBulkPrice(product, quantity);

        user.setBalance(user.getBalance() - finalPrice);
        merchantStock.setStock(merchantStock.getStock() - quantity);
        user.setPurchaseCount(user.getPurchaseCount() + 1);

        return finalPrice;
    }

    public boolean isLoyalCustomer(User user) {
        return user.getPurchaseCount() >= 2;
    }

    public double buyLoyaltyProduct(
            User user,
            Product product,
            MerchantStock merchantStock) {

        double finalPrice = product.getPrice() * 0.90;

        user.setBalance(user.getBalance() - finalPrice);
        merchantStock.setStock(merchantStock.getStock() - 1);
        user.setPurchaseCount(user.getPurchaseCount() + 1);

        return finalPrice;
    }

    public boolean hasValidReferral(User user) {

        if (user.getReferredBy() == null ||
                user.getReferredBy().isBlank()) {
            return false;
        }

        if (user.getId().equals(user.getReferredBy())) {
            return false;
        }

        return getUserById(user.getReferredBy()) != null;
    }

    public double buyReferralProduct(
            User user,
            Product product,
            MerchantStock merchantStock) {

        double finalPrice = product.getPrice() * 0.70;

        user.setBalance(user.getBalance() - finalPrice);
        merchantStock.setStock(merchantStock.getStock() - 1);
        user.setPurchaseCount(user.getPurchaseCount() + 1);

        return finalPrice;
    }

    public ApiResponse addBalance(User user, double amount) {
        user.setBalance(user.getBalance() + amount);
        return new ApiResponse("Balance added successfully");
    }
}
