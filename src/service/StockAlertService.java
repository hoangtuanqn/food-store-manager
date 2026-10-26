/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import repository.Repository;
import model.FoodProduct;
import java.util.List;
import java.util.stream.Collectors;

public class StockAlertService {

    private final Repository<FoodProduct, String> productRepository;

    public StockAlertService(Repository<FoodProduct, String> productRepository) {
        this.productRepository = productRepository;
    }

    public List<FoodProduct> getLowStockProducts() {
        return productRepository.findAll().stream()
                .filter(FoodProduct::isLowStock)
                .collect(Collectors.toList());
    }

    public List<FoodProduct> getExpiredProducts() {
        return productRepository.findAll().stream()
                .filter(FoodProduct::isExpired)
                .collect(Collectors.toList());
    }

    public List<FoodProduct> getNearExpiryProducts() {
        return productRepository.findAll().stream()
                .filter(FoodProduct::isNearExpiration)
                .collect(Collectors.toList());
    }

    public List<FoodProduct> getSellableProducts() {
        return productRepository.findAll().stream()
                .filter(product -> !product.isExpired() && product.getQuantity() > 0)
                .collect(Collectors.toList());
    }
}
