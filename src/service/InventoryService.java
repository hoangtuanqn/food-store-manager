/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import exception.*;
import java.time.LocalDateTime;
import model.FoodProduct;
import repository.FoodProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import model.MovementSource;
import model.StockMovement;
import repository.StockMovementRepository;

public class InventoryService {

    public static final int LOW_STOCK_THRESHOLD = 5;
    public static final int NEAR_EXPIRATION_DAYS = 7;
    private final FoodProductRepository productRepository;
     private final StockMovementRepository movementRepository;
    
    public InventoryService(FoodProductRepository productRepository,  StockMovementRepository movementRepository) {
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
    }

    public void checkSellable(String productId, int quantity) {
        FoodProduct p = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found: " + productId));
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero.");
        }
        if (p.isExpired()) {
            throw new ExpiredProductException("This food product has expired.");
        }
        if (p.getQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient stock.");
        }
    }

       public void deductInventory(String productId, int quantity) {
        deductInventory(productId, quantity, null);
    }

    // Bản mới: trừ kho sau khi bán và ghi log SALE kèm order ID
    public void deductInventory(String productId, int quantity, String orderId) {
        checkSellable(productId, quantity);
        FoodProduct p = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: " + productId));
        int oldQty = p.getQuantity();
        p.setQuantity(oldQty - quantity);
        productRepository.update(p);
        movementRepository.add(new StockMovement(productId, oldQty, p.getQuantity(),
                MovementSource.SALE, LocalDateTime.now(), orderId));
    }

    public void updateInventory(String productId, int newQuantity) {
        if (newQuantity < 0) {
            throw new InvalidQuantityException("Stock quantity cannot be negative");
        }
        FoodProduct p = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: " + productId));
        int oldQty = p.getQuantity();
        p.setQuantity(newQuantity);
        productRepository.update(p);
        if (oldQty != newQuantity) {
            movementRepository.add(new StockMovement(productId, oldQty, newQuantity,
                    MovementSource.MANUAL_UPDATE, LocalDateTime.now(), null));
        }
    }

    public List<StockMovement> getStockMovements() {
        return movementRepository.findAll();
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
                .filter(p -> !p.isExpired() && p.getQuantity() > 0)
                .collect(Collectors.toList());
    }
}
