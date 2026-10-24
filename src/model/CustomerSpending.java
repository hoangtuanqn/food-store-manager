/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ad
 */
public class CustomerSpending {

    private final String customerId;
    private final String customerName;
    private final double totalPurchase;

    public CustomerSpending(String customerId, String customerName, double totalPurchase) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.totalPurchase = totalPurchase;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getTotalPurchase() {
        return totalPurchase;
    }
}
