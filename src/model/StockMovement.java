/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import helper.IdHelper;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author ad
 */
public class StockMovement implements Serializable {

    private final String productId;
    private final int oldQuantity;
    private final int newQuantity;
    private final MovementSource source;
    private final LocalDateTime timestamp;
    private final String orderId;

    public StockMovement(String productId, int oldQuantity, int newQuantity,
            MovementSource source, LocalDateTime timestamp, String orderId) {
        this.productId = IdHelper.normalize(productId);
        this.oldQuantity = oldQuantity;
        this.newQuantity = newQuantity;
        this.source = source;
        this.timestamp = timestamp;
        this.orderId = IdHelper.normalize(orderId);
    }

    public String getProductId() {
        return productId;
    }

    public int getOldQuantity() {
        return oldQuantity;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    public MovementSource getSource() {
        return source;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getOrderId() {
        return orderId;
    }
}
