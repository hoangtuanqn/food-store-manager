/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.controller;
import foodstore.exception.BusinessException;
import foodstore.model.Customer;
import foodstore.model.MembershipType;
import foodstore.service.CustomerService;
import foodstore.util.InputHelper;
import foodstore.view.ConsoleView;
import foodstore.view.CustomerView;
import java.util.List;
/**
 *
 * @author MSI
 */
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void run() {
        while (true) {
            CustomerView.showMenu();

            int choice = InputHelper.readIntRange(
                    "Choose an option: ", 0, 6
            );

            switch (choice) {

                case 1:
                    addCustomer();
                    break;

                case 2:
                    updateCustomer();
                    break;

                case 3:
                    deleteCustomer();
                    break;

                case 4:
                    viewAllCustomers();
                    break;

                case 5:
                    searchCustomer();
                    break;

                case 6:
                    calculateDiscount();
                    break;

                case 0:
                    return;
            }
        }
    }

    private void addCustomer() {

        CustomerView.showAddHeader();

        String id;

        while (true) {
            id = InputHelper.readString("Customer ID: ");

            if (customerService.checkIdExist(id)) {
                ConsoleView.showError(
                        "Customer ID already exists!"
                );
            } else {
                break;
            }
        }

        String name = InputHelper.readString("Full Name: ");
        String phone = InputHelper.readString("Phone: ");
        String address = InputHelper.readString("Address: ");

        MembershipType membershipType = readMembershipType();

        Customer customer = new Customer(
                id,
                name,
                phone,
                address,
                membershipType
        );

        try {
            customerService.addCustomer(customer);

            ConsoleView.showSuccess(
                    "Customer added successfully."
            );

        } catch (BusinessException e) {
            ConsoleView.showError(e.getMessage());
        }
    }

    private void updateCustomer() {

        CustomerView.showUpdateHeader();

        String id = InputHelper.readString(
                "Customer ID to update: "
        );

        Customer oldCustomer = customerService.findById(id);

        if (oldCustomer == null) {
            ConsoleView.showError(
                    "Customer ID does not exist."
            );
            return;
        }

        System.out.println("Current information:");
        CustomerView.showCustomer(oldCustomer);

        String name = InputHelper.readString(
                "New Full Name: "
        );

        String phone = InputHelper.readString(
                "New Phone: "
        );

        String address = InputHelper.readString(
                "New Address: "
        );

        MembershipType membershipType = readMembershipType();

        Customer updatedCustomer = new Customer(
                id,
                name,
                phone,
                address,
                membershipType
        );

        try {
            customerService.updateCustomer(updatedCustomer);

            ConsoleView.showSuccess(
                    "Customer updated successfully."
            );

        } catch (BusinessException | IllegalArgumentException e) {
            ConsoleView.showError(e.getMessage());
        }
    }

    private void deleteCustomer() {

        CustomerView.showDeleteHeader();

        String id = InputHelper.readString(
                "Customer ID to delete: "
        );

        Customer customer = customerService.findById(id);

        if (customer == null) {
            ConsoleView.showError(
                    "Customer ID does not exist."
            );
            return;
        }

        System.out.println("Customer to delete:");
        CustomerView.showCustomer(customer);

        String confirm = InputHelper.readString(
                "Are you sure? (Y/N): "
        );

        if (!confirm.equalsIgnoreCase("Y")) {
            ConsoleView.showSuccess(
                    "Delete operation cancelled."
            );
            return;
        }

        try {
            customerService.deleteCustomer(id);

            ConsoleView.showSuccess(
                    "Customer deleted successfully."
            );

        } catch (IllegalArgumentException e) {
            ConsoleView.showError(e.getMessage());
        }
    }

    private void viewAllCustomers() {

        List<Customer> customers =
                customerService.getAllCustomers();

        CustomerView.showCustomers(customers);
    }

    private void searchCustomer() {

        CustomerView.showSearchHeader();

        String keyword = InputHelper.readString(
                "Enter customer name or phone: "
        );

        List<Customer> customers =
                customerService.search(keyword);

        CustomerView.showCustomers(customers);
    }

    private void calculateDiscount() {

        CustomerView.showDiscountHeader();

        String id = InputHelper.readString(
                "Customer ID: "
        );

        Customer customer = customerService.findById(id);

        if (customer == null) {
            ConsoleView.showError(
                    "Customer ID does not exist."
            );
            return;
        }

        double subtotal = InputHelper.readPrice(
                "Subtotal: "
        );

        double discount =
                customer.calculateDiscount(subtotal);

        double finalAmount =
                subtotal - discount;

        System.out.println();
        System.out.println("Customer: "
                + customer.getFullName());

        System.out.println("Membership: "
                + customer.getMembershipType());

        System.out.printf(
                "Subtotal: %.2f%n",
                subtotal
        );

        System.out.printf(
                "Discount: %.2f%n",
                discount
        );

        System.out.printf(
                "Final Amount: %.2f%n",
                finalAmount
        );
    }

    private MembershipType readMembershipType() {

        System.out.println("Membership Type:");
        System.out.println("1. Regular");
        System.out.println("2. VIP");

        int choice = InputHelper.readIntRange(
                "Choose membership type: ",
                1,
                2
        );

        if (choice == 1) {
            return MembershipType.REGULAR;
        }

        return MembershipType.VIP;
    }
}
