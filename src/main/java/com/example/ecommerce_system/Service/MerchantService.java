package com.example.ecommerce_system.Service;

import com.example.ecommerce_system.Model.Merchant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MerchantService {

    private ArrayList<Merchant> merchants = new ArrayList<>();

    // Add a new merchant
    public void addMerchant(Merchant merchant) {
        merchants.add(merchant);
    }

    // Get all merchants
    public ArrayList<Merchant> getAllMerchants() {
        return merchants;
    }

    // Get merchant by ID
    public Merchant getMerchantById(String id) {
        for (Merchant merchant : merchants) {
            if (merchant.getId().equals(id)) {
                return merchant;
            }
        }
        return null;
    }

    // Update merchant
    public boolean updateMerchant(String id, Merchant updatedMerchant) {
        for (int i = 0; i < merchants.size(); i++) {
            if (merchants.get(i).getId().equals(id)) {
                merchants.set(i, updatedMerchant);
                return true;
            }
        }
        return false;
    }

    // Delete merchant
    public boolean deleteMerchant(String id) {
        for (int i = 0; i < merchants.size(); i++) {
            if (merchants.get(i).getId().equals(id)) {
                merchants.remove(i);
                return true;
            }
        }
        return false;
    }
}