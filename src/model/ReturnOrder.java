/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import helper.IdHelper;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ad
 */
public class ReturnOrder implements Serializable {

    private final String returnId;
    private final String orderId;
    private final String reason;
    private final LocalDate returnDate;
    private final double discountRate;
    private final List<ReturnDetail> details;

    public ReturnOrder(String returnId, String orderId, String reason, LocalDate returnDate,
            double discountRate, List<ReturnDetail> details) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Return reason is required.");
        }
        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException("A return must contain at least one product.");
        }
        this.returnId = returnId;
        this.orderId = IdHelper.normalize(orderId);
        this.reason = reason.trim();
        this.returnDate = returnDate;
        this.discountRate = discountRate;
        this.details = new ArrayList<>(details);
    }

    public String getReturnId() {
        return returnId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getReason() {
        return reason;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public List<ReturnDetail> getDetails() {
        return new ArrayList<>(details);
    }

    public double getSubtotal() {
        return details.stream().mapToDouble(ReturnDetail::getLineTotal).sum();
    }

    public double getRefundAmount() {
        return getSubtotal() * (1 - discountRate);
    }
}
