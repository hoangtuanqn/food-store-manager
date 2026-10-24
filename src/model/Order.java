/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import exception.InvalidTransactionException;
import exception.NotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MSI
 */
public class Order implements Serializable {

    private final String orderId;
    private final Customer customer;
    private final LocalDate orderDate;
    private final List<OrderDetail> details;
    private OrderStatus status;
    private double discountAmount;

    private String customerNameAtSale;
    private MembershipType membershipTypeAtSale;
    private double discountRateAtSale;

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

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getCustomerId() {
        return customer.getCustomerId();
    }

    public String getCustomerName() {
        return isCompleted() ? customerNameAtSale : customer.getFullName();
    }

    public MembershipType getMembershipType() {
        return isCompleted() ? membershipTypeAtSale : customer.getMembershipType();
    }

    public double getDiscountRate() {
        return isCompleted() ? discountRateAtSale : customer.getDiscountRate();
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

    public boolean isCompleted() {
        return status == OrderStatus.COMPLETED;
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
        return details.stream()
                .filter(d -> d.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public void addDetail(OrderDetail detail) {
        requirePending();
        if (detail == null) {
            throw new IllegalArgumentException("Order detail is required.");
        }
        OrderDetail existing = findDetail(detail.getProductId());
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
        customerNameAtSale = customer.getFullName();
        membershipTypeAtSale = customer.getMembershipType();
        discountRateAtSale = customer.getDiscountRate();
        discountAmount = customer.calculateDiscount(getSubtotal());
        status = OrderStatus.COMPLETED;
    }

    public void cancel() {
        requirePending();
        status = OrderStatus.CANCELLED;
    }

    public int getTotalQuantity() {
        return details.stream().mapToInt(OrderDetail::getQuantity).sum();
    }

    public double getSubtotal() {
        return details.stream().mapToDouble(OrderDetail::getLineTotal).sum();
    }

    public double getDiscount() {
        if (isCompleted()) {
            return discountAmount;
        }
        return customer.calculateDiscount(getSubtotal());
    }

    public double getFinalAmount() {
        return getSubtotal() - getDiscount();
    }
}
