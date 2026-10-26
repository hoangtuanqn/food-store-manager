/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import exception.BusinessException;
import model.Customer;
import model.MembershipType;
import service.CustomerService;
import helper.InputHelper;
import view.ConsoleView;
import view.CustomerView;
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
                    "Choose an option: ", 0, 5
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

                case 0:
                    return;
            }
        }
    }

    private void addCustomer() {

        CustomerView.showAddHeader();

        String id;

        while (true) {
            id = InputHelper.readId("Customer ID: ");

            if (customerService.checkIdExist(id)) {
                ConsoleView.showError(
                        "Customer ID already exists!"
                );
            } else {
                break;
            }
        }

        String name = InputHelper.readString("Full Name: ");
        String phone = readPhone(id, null);
        String address = InputHelper.readString("Address: ");

        MembershipType membershipType = readMembershipType();

        Customer customer = Customer.create(
                id,
                name,
                phone,
                address,
                membershipType
        );
        
        boolean isConfirm = InputHelper.confirmAction("Add");
        if (!isConfirm) {
            return;
        }

        try {
            customerService.addCustomer(customer);

            ConsoleView.showSuccess(
                    "Customer added successfully."
            );

        } catch (BusinessException | IllegalArgumentException e) {
            ConsoleView.showError(e.getMessage());
        }
    }

    private void updateCustomer() {

        CustomerView.showUpdateHeader();

        String id = InputHelper.readId(
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

        System.out.println("Press Enter to keep the current value.");

        String name = InputHelper.readStringOrDefault(
                "Full Name ", oldCustomer.getFullName()
        );

        String phone = readPhone(id, oldCustomer.getPhone());

        String address = InputHelper.readStringOrDefault(
                "Address ", oldCustomer.getAddress()
        );

        MembershipType membershipType
                = readMembershipType(oldCustomer.getMembershipType());

        boolean isConfirm = InputHelper.confirmAction("Update");
        if (!isConfirm) {
            return;
        }
        
        try {
            customerService.updateCustomer(id, name, phone, address, membershipType);

            ConsoleView.showSuccess(
                    "Customer updated successfully."
            );

        } catch (BusinessException | IllegalArgumentException e) {
            ConsoleView.showError(e.getMessage());
        }
    }

    private void deleteCustomer() {

        CustomerView.showDeleteHeader();

        String id = InputHelper.readId(
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

        boolean isConfirm = InputHelper.confirmAction("Delete");
        if (!isConfirm) {
            return;
        }
        
        try {
            customerService.deleteCustomer(id);

            ConsoleView.showSuccess(
                    "Customer deleted successfully."
            );

        } catch (BusinessException | IllegalArgumentException e) {
            ConsoleView.showError(e.getMessage());
        }
    }

    private void viewAllCustomers() {

        List<Customer> customers
                = customerService.getAllCustomers();

        CustomerView.showCustomers(customers);
    }

    private void searchCustomer() {

        CustomerView.showSearchHeader();

        String keyword = InputHelper.readString(
                "Enter customer name or phone: "
        );

        List<Customer> customers
                = customerService.search(keyword);

        CustomerView.showCustomers(customers);
    }

    private String readPhone(String customerId, String currentPhone) {
        while (true) {
            String input = currentPhone == null
                    ? InputHelper.readString("Phone: ")
                    : InputHelper.readStringOrDefault("Phone ", currentPhone);
            try {
                return customerService.validatePhone(input, customerId);
            } catch (BusinessException | IllegalArgumentException e) {
                ConsoleView.showError(e.getMessage());
            }
        }
    }

    private MembershipType readMembershipType() {

        CustomerView.showMemberShipType();

        int choice = InputHelper.readIntRange(
                "Choose membership type: ",
                1,
                2
        );

        return choice == 1 ? MembershipType.REGULAR : MembershipType.VIP;
    }

    private MembershipType readMembershipType(MembershipType current) {

        CustomerView.showMemberShipType();

        int currentChoice = current == MembershipType.REGULAR ? 1 : 2;

        int choice = InputHelper.readIntRangeOrDefault(
                "Choose membership type ",
                1,
                2,
                currentChoice
        );

        return choice == 1 ? MembershipType.REGULAR : MembershipType.VIP;
    }
}
