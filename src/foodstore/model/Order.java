/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.model;

import foodstore.exception.InvalidTransactionException;
import foodstore.exception.NotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MSI
 */
public class Order {

    private final String orderId;
    private final Customer customer;
    private final LocalDate orderDate;
    private final List<OrderDetail> details;
    private OrderStatus status;
    private double discountAmount;

    public Order(String orderId, Customer customer, LocalDate orderDate) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID must not be empty.");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer is required.");
        }
        if (orderDate == null) {
            throw new IllegalArgumentException("Order date is required.");
        }
        this.orderId = orderId;
        this.customer = customer;
        this.orderDate = orderDate;
        this.details = new ArrayList<>();
        this.status = OrderStatus.PENDING;
    }

    // Getter
    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public List<OrderDetail> getDetails() {
        return new ArrayList<>(details);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public boolean isPending() {
        return status == OrderStatus.PENDING;
    }

    public boolean isEmpty() {
        return details.isEmpty();
    }

    private void requirePending() {
        if (status != OrderStatus.PENDING) {
            throw new InvalidTransactionException("Transaction " + orderId + " is " + status + " and cannot be changed.");
        }
    }

    public OrderDetail findDetail(String productId) {
        for (OrderDetail d : details) {
            if (d.getProduct().getProductId().equals(productId)) {
                return d;
            }
        }
        return null;
    }

    public void addDetail(OrderDetail detail) {
        requirePending();
        if (detail == null) {
            throw new IllegalArgumentException("Order detail is required.");
        }
        OrderDetail existing = findDetail(detail.getProduct().getProductId());
        if (existing != null) {
            existing.increaseQuantity(detail.getQuantity());
        } else {
            details.add(detail);
        }
    }

    public void removeDetail(String productId) {
        requirePending();
        OrderDetail existing = findDetail(productId);
        if (existing == null) {
            throw new NotFoundException("Product ID " + productId + " is not in this transaction.");
        }
        details.remove(existing);
    }

    public void complete() {
        requirePending();
        if (details.isEmpty()) {
            throw new InvalidTransactionException("A sales transaction must contain at least one product.");
        }
        discountAmount = customer.calculateDiscount(getSubtotal());
        status = OrderStatus.COMPLETED;
    }

    public void cancel() {
        requirePending();
        status = OrderStatus.CANCELLED;
    }

    public int getTotalQuantity() {
        int sum = 0;
        for (OrderDetail d : details) {
            sum += d.getQuantity();
        }
        return sum;
    }

    public double getSubtotal() {
        double sum = 0;
        for (OrderDetail d : details) {
            sum += d.getLineTotal();
        }
        return sum;
    }

    public double getDiscount() {
        if (status == OrderStatus.COMPLETED) {
            return discountAmount;
        }
        return customer.calculateDiscount(getSubtotal());
    }

    public double getFinalAmount() {
        return getSubtotal() - getDiscount();
    }
}
