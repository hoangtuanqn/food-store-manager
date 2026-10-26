/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import exception.*;
import model.FoodProduct;
import model.MovementSource;
import model.StockMovement;
import repository.FoodProductRepository;
import repository.StockMovementRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryService {

    public static final int LOW_STOCK_THRESHOLD = 5;
    public static final int NEAR_EXPIRATION_DAYS = 7;
    private final FoodProductRepository productRepository;
    private final StockMovementRepository movementRepository;

    public InventoryService(FoodProductRepository productRepository, StockMovementRepository movementRepository) {
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
    }

    public void checkSellable(String productId, int quantity) {
        FoodProduct product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found: " + productId));
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero.");
        }
        if (product.isExpired()) {
            throw new ExpiredProductException("This food product has expired.");
        }
        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient stock.");
        }
    }

    public void deductInventory(String productId, int quantity, String orderId) {
        FoodProduct product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found: " + productId));
        int oldQuantity = product.getQuantity();
        product.setQuantity(oldQuantity - quantity);
        productRepository.update(product);
        recordMovement(productId, oldQuantity, product.getQuantity(), MovementSource.SALE, orderId);
    }

    public void restockFromReturn(String productId, int quantity, String orderId) {
        FoodProduct product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found: " + productId));
        int oldQuantity = product.getQuantity();
        product.setQuantity(oldQuantity + quantity);
        productRepository.update(product);
        recordMovement(productId, oldQuantity, product.getQuantity(), MovementSource.RETURN, orderId);
    }

    public void updateInventory(String productId, int newQuantity) {
        if (newQuantity < 0) {
            throw new InvalidQuantityException("Stock quantity cannot be negative");
        }
        FoodProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: " + productId));
        int oldQuantity = product.getQuantity();
        product.setQuantity(newQuantity);
        productRepository.update(product);
        recordManualUpdate(productId, oldQuantity, newQuantity);
    }

    public void recordManualUpdate(String productId, int oldQuantity, int newQuantity) {
        recordMovement(productId, oldQuantity, newQuantity, MovementSource.MANUAL_UPDATE, null);
    }

    public List<StockMovement> getStockMovements() {
        return movementRepository.findAll();
    }

    private void recordMovement(String productId, int oldQuantity, int newQuantity, MovementSource source, String orderId) {
        if (oldQuantity != newQuantity) {
            movementRepository.add(new StockMovement(productId, oldQuantity, newQuantity, source, LocalDateTime.now(), orderId));
        }
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
