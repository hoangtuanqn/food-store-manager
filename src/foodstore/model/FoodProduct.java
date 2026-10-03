/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author MSI
 */
public class FoodProduct {

    public static final int LOW_STOCK_THRESHOLD = 5;
    public static final int NEAR_EXPIRATION_DAYS = 7;

    private final String productId;
    private String name;
    private String category;
    private String unit;
    private double price;
    private int quantity;
    private LocalDate productionDate;
    private LocalDate expirationDate;

    // constructor
    public FoodProduct(String productId, String name, String category, String unit,
            double price, int quantity,
            LocalDate productionDate, LocalDate expirationDate) {
        this.productId = requireText(productId, "Product ID");
        this.name = requireText(name, "Product name");
        this.category = requireText(category, "Category");
        this.unit = requireText(unit, "Unit");
        this.price = requirePrice(price);
        this.quantity = requireStock(quantity);
        requireDates(productionDate, expirationDate);
        this.productionDate = productionDate;
        this.expirationDate = expirationDate;
    }

    // Getters
    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getUnit() {
        return unit;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    // Setter
    public void setName(String name) {
        this.name = requireText(name, "Product name");
    }

    public void setCategory(String category) {
        this.category = requireText(category, "Category");
    }

    public void setUnit(String unit) {
        this.unit = requireText(unit, "Unit");
    }

    public void setPrice(double price) {
        this.price = requirePrice(price);
    }

    public void setQuantity(int quantity) {
        this.quantity = requireStock(quantity);
    }

    public void setDates(LocalDate productionDate, LocalDate expirationDate) {
        requireDates(productionDate, expirationDate);
        this.productionDate = productionDate;
        this.expirationDate = expirationDate;
    }

    // method
    public boolean isExpiredOn(LocalDate date) {
        return date.isAfter(expirationDate);
    }

    public boolean isExpired() {
        return isExpiredOn(LocalDate.now());
    }

    public boolean isLowStock() {
        return quantity <= LOW_STOCK_THRESHOLD;
    }

    public long daysUntilExpiration() {
        return ChronoUnit.DAYS.between(LocalDate.now(), expirationDate);
    }

    public boolean isNearExpiration() {
        long days = daysUntilExpiration();
        return days >= 0 && days <= NEAR_EXPIRATION_DAYS;
    }

    // Validate
    private static String requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " must not be empty.");
        }
        return value.trim();
    }

    private static double requirePrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        return price;
    }

    private static int requireStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative.");
        }
        return quantity;
    }

    public static void requireDates(LocalDate production, LocalDate expiration) {
        if (production == null || expiration == null) {
            throw new IllegalArgumentException("Production date and expiration date are required.");
        }
        if (production.isAfter(expiration)) {
            throw new IllegalArgumentException("Production date cannot be after expiration date.");
        }
    }

    // Object
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodProduct)) {
            return false;
        }
        return productId.equals(((FoodProduct) o).productId);
    }

    @Override
    public int hashCode() {
        return productId.hashCode();
    }

    @Override
    public String toString() {
        return String.format("FoodProduct{id=%s, name=%s, category=%s, unit=%s, price=%.0f, qty=%d, mfg=%s, exp=%s}",
                productId, name, category, unit, price, quantity, productionDate, expirationDate);
    }

}
