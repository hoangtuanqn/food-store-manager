/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
/**
 *
 * @author MSI
 */
public class OrderDetail implements Serializable {

    private final String productId;
    private final String productName;
    private final String unit;
    private int quantity;
    private final double unitPrice;

    public OrderDetail(FoodProduct product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product is required.");
        }
        this.productId = product.getProductId();
        this.productName = product.getName();
        this.unit = product.getUnit();
        this.unitPrice = product.getPrice();
        setQuantity(quantity);
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getUnit() {
        return unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        this.quantity = quantity;
    }

    public void increaseQuantity(int amount) {
        setQuantity(this.quantity + amount);
    }

    public double getLineTotal() {
        return unitPrice * quantity;
    }

    @Override
    public String toString() {
        return String.format("%-6s %-18s %4d %,10.0f %,12.0f",
                productId, productName, quantity, unitPrice, getLineTotal());
    }
}
