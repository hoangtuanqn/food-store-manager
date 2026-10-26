/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author MSI
 */
public class RegularCustomer extends Customer {

    public RegularCustomer(String customerId, String fullName, String phone, String address) {
        super(customerId, fullName, phone, address, MembershipType.REGULAR);
    }

    @Override
    public double getDiscountRate() {
        return 0;
    }
}
