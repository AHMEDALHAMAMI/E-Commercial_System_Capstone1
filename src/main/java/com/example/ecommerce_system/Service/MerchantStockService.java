package com.example.ecommerce_system.Service;

import com.example.ecommerce_system.Model.MerchantStock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MerchantStockService {

    private ArrayList<MerchantStock> merchantStocks = new ArrayList<>();

    // Add a new merchant stock
    public void addMerchantStock(MerchantStock merchantStock) {
        merchantStocks.add(merchantStock);
    }

    // Get all merchant stocks
    public ArrayList<MerchantStock> getAllMerchantStocks() {
        return merchantStocks;
    }

    // Get merchant stock by ID
    public MerchantStock getMerchantStockById(String id) {
        for (MerchantStock merchantStock : merchantStocks) {
            if (merchantStock.getId().equals(id)) {
                return merchantStock;
            }
        }
        return null;
    }

    // Find stock using product ID and merchant ID
    public MerchantStock getMerchantStockByProductAndMerchant(String productId, String merchantId) {

        for (MerchantStock merchantStock : merchantStocks) {
            if (merchantStock.getProductId().equals(productId)
                    && merchantStock.getMerchantId().equals(merchantId)) {
                return merchantStock;
            }
        }
        return null;
    }

    // Add additional stock to an existing merchant stock
    public boolean addAdditionalStock(String productId, String merchantId, int additionalStock) {

        MerchantStock merchantStock =
                getMerchantStockByProductAndMerchant(productId, merchantId);

        if (merchantStock == null) {
            return false;
        }

        merchantStock.setStock(merchantStock.getStock() + additionalStock);

        return true;
    }


    // Update merchant stock
    public boolean updateMerchantStock(String id, MerchantStock updatedMerchantStock) {

        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getId().equals(id)) {
                merchantStocks.set(i, updatedMerchantStock);
                return true;
            }
        }
        return false;
    }

    // Delete merchant stock
    public boolean deleteMerchantStock(String id) {

        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getId().equals(id)) {
                merchantStocks.remove(i);
                return true;
            }
        }
        return false;
    }


    //==================== Extra EndPoints =====================
    // Filter merchant stocks by quantity
    public ArrayList<MerchantStock> getMerchantStocksByStockGreaterThan(int stock) {

        ArrayList<MerchantStock> filteredMerchantStocks = new ArrayList<>();

        for (MerchantStock merchantStock : merchantStocks) {
            if (merchantStock.getStock() > stock) {
                filteredMerchantStocks.add(merchantStock);
            }
        }

        return filteredMerchantStocks;
    }

}