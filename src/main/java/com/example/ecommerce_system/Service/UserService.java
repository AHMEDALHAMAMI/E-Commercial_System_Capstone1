package com.example.ecommerce_system.Service;

import com.example.ecommerce_system.Model.MerchantStock;
import com.example.ecommerce_system.Model.Product;
import com.example.ecommerce_system.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    private final ArrayList<User> users = new ArrayList<>();

    private final ArrayList<String> purchaseUsers = new ArrayList<>();

    private final ArrayList<String[]> referrals = new ArrayList<>();

    public ArrayList<User> getAllUsers() {
        return users;
    }

    public User addUser(User user) {
        users.add(user);
        return user;
    }

    public User getUserById(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }

        return null;
    }

    public User updateUser(String id, User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                users.set(i, user);
                return user;
            }
        }

        return null;
    }

    public boolean deleteUser(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                users.remove(user);
                return true;
            }
        }

        return false;
    }

    public double buyProduct(
            User user,
            Product product,
            MerchantStock merchantStock) {

        double finalPrice = product.getPrice();

        user.setBalance(user.getBalance() - finalPrice);

        merchantStock.setStock(merchantStock.getStock() - 1);

        purchaseUsers.add(user.getId());

        return finalPrice;
    }

    public double buySaudiNationalDay(
            User user,
            Product product,
            MerchantStock merchantStock) {

        double finalPrice = product.getPrice() * 0.04;

        user.setBalance(user.getBalance() - finalPrice);

        merchantStock.setStock(merchantStock.getStock() - 1);

        purchaseUsers.add(user.getId());

        return finalPrice;
    }

    public double buyBulkProducts(
            User user,
            Product product,
            MerchantStock merchantStock,
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

        user.setBalance(user.getBalance() - finalPrice);

        merchantStock.setStock(merchantStock.getStock() - quantity);

        purchaseUsers.add(user.getId());

        return finalPrice;
    }

    public boolean isLoyalCustomer(String userId) {

        int purchaseCount = 0;

        for (String id : purchaseUsers) {
            if (id.equals(userId)) {
                purchaseCount++;
            }
        }

        return purchaseCount > 1;
    }

    public void addReferral(String userId, String referrerId) {

        referrals.add(new String[]{userId, referrerId});
    }

    public boolean hasValidReferral(String userId) {

        for (String[] referral : referrals) {

            if (referral[0].equals(userId)) {

                User referrer = getUserById(referral[1]);

                if (referrer != null && !referral[1].equals(userId)) {
                    return true;
                }
            }
        }

        return false;
    }

    public double buyLoyaltyProduct(
            User user,
            Product product,
            MerchantStock merchantStock) {

        double finalPrice = product.getPrice() * 0.90;

        user.setBalance(user.getBalance() - finalPrice);

        merchantStock.setStock(merchantStock.getStock() - 1);

        purchaseUsers.add(user.getId());

        return finalPrice;
    }

    public double buyReferralProduct(
            User user,
            Product product,
            MerchantStock merchantStock) {

        double finalPrice = product.getPrice() * 0.70;

        user.setBalance(user.getBalance() - finalPrice);

        merchantStock.setStock(merchantStock.getStock() - 1);

        purchaseUsers.add(user.getId());

        return finalPrice;
    }

    public boolean addBalance(String userId, double amount) {

        User user = getUserById(userId);

        if (user == null || amount <= 0) {
            return false;
        }

        user.setBalance(user.getBalance() + amount);

        return true;
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
}
