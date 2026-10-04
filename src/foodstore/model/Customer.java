/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.model;

/**
 *
 * @author MSI
 */
public class Customer {

    private final String customerId;
    private String fullName;
    private String phone;
    private String address;
    private MembershipType membershipType;

    public Customer(String customerId, String fullName, String phone,
                    String address, MembershipType membershipType) {

        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID must not be empty.");
        }

        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name must not be empty.");
        }

        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone must not be empty.");
        }

        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address must not be empty.");
        }

        if (membershipType == null) {
            throw new IllegalArgumentException("Membership type must not be null.");
        }

        this.customerId = customerId.trim();
        this.fullName = fullName.trim();
        this.phone = phone.trim();
        this.address = address.trim();
        this.membershipType = membershipType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name must not be empty.");
        }
        this.fullName = fullName.trim();
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone must not be empty.");
        }
        this.phone = phone.trim();
    }

    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address must not be empty.");
        }
        this.address = address.trim();
    }

    public void setMembershipType(MembershipType membershipType) {
        if (membershipType == null) {
            throw new IllegalArgumentException("Membership type must not be null.");
        }
        this.membershipType = membershipType;
    }

    public double calculateDiscount(double subtotal) {
        if (subtotal < 0) {
            throw new IllegalArgumentException("Subtotal cannot be negative.");
        }

        if (membershipType == MembershipType.VIP) {
            return subtotal * 0.10;
        }

        return 0;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %s | Name: %s | Phone: %s | Address: %s | Membership: %s",
                customerId,
                fullName,
                phone,
                address,
                membershipType
        );
    }
}
