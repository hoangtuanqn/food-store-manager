/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ad
 */
public class ReturnDetail {

    private final String productId;
    private final String productName;
    private final double unitPrice;
    private final int quantity;
    private final boolean restocked;

    public ReturnDetail(OrderDetail source, int quantity, boolean restocked) {
        if (source == null) {
            throw new IllegalArgumentException("Order detail is required.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        this.productId = source.getProductId();
        this.productName = source.getProductName();
        this.unitPrice = source.getUnitPrice();
        this.quantity = quantity;
        this.restocked = restocked;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isRestocked() {
        return restocked;
    }

    public double getLineTotal() {
        return unitPrice * quantity;
    }
}
