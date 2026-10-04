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

        System.out.println();
        System.out.println("CUSTOMER LIST");
        System.out.println("--------------------------------------------------------------------------------");

        for (Customer customer : customers) {
            System.out.println(customer);
        }

        System.out.println("--------------------------------------------------------------------------------");
    }

    public static void showCustomer(Customer customer) {
        if (customer == null) {
            ConsoleView.showError("Customer not found.");
            return;
        }

        System.out.println(customer);
    }
}
