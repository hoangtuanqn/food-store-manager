/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.view;
import foodstore.model.Customer;
import java.util.List;
/**
 *
 * @author MSI
 */
public class CustomerView {
    private static final String ROW_FORMAT = "%-7s%-19s%-15s%-10s%n";
    
    public static void showMenu() {
        MenuView.showSubMenu(
                "CUSTOMER MANAGEMENT",
                new String[]{
                    "Add Customer",
                    "Update Customer",
                    "Delete Customer",
                    "View All Customers",
                    "Search Customer",
                    "Calculate Discount"
                }
        );
    }

    public static void showAddHeader() {
        MenuView.showTitle("ADD CUSTOMER");
    }

    public static void showUpdateHeader() {
        MenuView.showTitle("UPDATE CUSTOMER");
    }

    public static void showDeleteHeader() {
        MenuView.showTitle("DELETE CUSTOMER");
    }

    public static void showSearchHeader() {
        MenuView.showTitle("SEARCH CUSTOMER");
    }

    public static void showDiscountHeader() {
        MenuView.showTitle("CALCULATE DISCOUNT");
    }

    public static void showCustomers(List<Customer> customers) {
        if (customers == null || customers.isEmpty()) {
            ConsoleView.showError("No customers found.");
            return;
        }
        MenuView.showTitle("CUSTOMER LIST");
        System.out.printf(ROW_FORMAT, "ID", "Name", "Phone", "Type");
        MenuView.showLine();
        for (Customer c : customers) {
            System.out.printf(ROW_FORMAT,
                    c.getCustomerId(),
                    c.getFullName(),
                    c.getPhone(),
                    c.getMembershipType());
        }
        MenuView.showLine();
    }

    public static void showCustomer(Customer customer) {
        if (customer == null) {
            ConsoleView.showError("Customer not found.");
            return;
        }

        System.out.println(customer);
    }

    public static void showMemberShipType() {
        System.out.println("Membership Type:");
        System.out.println("1. Regular");
        System.out.println("2. VIP");
    }
}
