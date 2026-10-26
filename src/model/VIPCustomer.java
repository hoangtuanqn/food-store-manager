/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author MSI
 */
public class VIPCustomer extends Customer {

    public VIPCustomer(String customerId, String fullName, String phone, String address) {
        super(customerId, fullName, phone, address, MembershipType.VIP);
    }

    @Override
    public double getDiscountRate() {
        return 0.10;
    }
}
