/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.controller;

import foodstore.exception.BusinessException;
import foodstore.model.FoodProduct;
import foodstore.model.Order;
import foodstore.service.CustomerService;
import foodstore.service.ProductService;
import foodstore.service.SalesService;
import foodstore.util.InputHelper;
import foodstore.view.ConsoleView;
import foodstore.view.SalesView;
import java.time.LocalDate;

/**
 *
 * @author MSI
 */
public class SalesController {

    private final SalesService salesService;
    private final CustomerService customerService;
    private final ProductService productService;

    public SalesController(SalesService salesService, CustomerService customerService, ProductService productService) {
        this.salesService = salesService;
        this.customerService = customerService;
        this.productService = productService;
    }

    public void run() {
        while (true) {
            SalesView.showMenu();

            int choice = InputHelper.readIntRange(
                    "Choose an option: ", 0, 8
            );

            switch (choice) {
                case 1:
                    createSalesTransaction();
                    break;
                case 2:
                    addProductToTransaction();
                    break;
                case 3:
                    removeProductFromTransaction();
                    break;
                case 4:
                    viewTransactionDetail();
                    break;
                case 5:
                    viewTransactionHistory();
                    break;
                case 6:
                    searchTransactionsByCustomer();
                    break;
                case 7:
                    cancelTransaction();
                    break;
                case 8:
                    deleteTransaction();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void createSalesTransaction() {
        SalesView.showCreateSalesTransactionHeader();

        String id;
        while (true) {
            id = InputHelper.readString("Transaction ID: ");

            if (salesService.checkIdExist(id)) {
                ConsoleView.showError(
                        "Failed to create transaction. Transaction ID already exists."
                );
            } else {
                break;
            }
        }
        String customerId;
        while (true) {
            customerId = InputHelper.readString("Customer ID: ");

            if (!customerService.checkIdExist(customerId)) {
                ConsoleView.showError(
                        "Failed to create transaction. Customer ID does not exist."
                );
            } else {
                break;
            }
        }

        LocalDate date = InputHelper.readDate("Date: ");

        if (!InputHelper.confirmAction("Continue")) {
            return;
        }

        try {
            salesService.createTransaction(id, customerId, date);
            ConsoleView.showSuccess(
                    "Sales transaction created successfully."
            );
        } catch (BusinessException | IllegalArgumentException e) {
            ConsoleView.showError("Failed to create transaction. " + e.getMessage());
        }
    }

    private void addProductToTransaction() {
        SalesView.showAddProductHeader();
        Order order = findPendingTransaction();
        if (order != null) {
            addProductsLoop(order.getOrderId());
        }
    }

    private void removeProductFromTransaction() {
        SalesView.showRemoveProductHeader();
        Order order = findPendingTransaction();
        if (order == null) {
            return;
        }
        SalesView.showBill(order);
        if (order.isEmpty()) {
            return;
        }

        String productId = InputHelper.readString("Product ID: ");
        if (!InputHelper.confirmAction("Remove")) {
            return;
        }
        try {
            salesService.removeProduct(order.getOrderId(), productId);
            ConsoleView.showSuccess("Product removed from transaction successfully.");
        } catch (BusinessException | IllegalArgumentException e) {
            ConsoleView.showError("Failed to remove product. " + e.getMessage());
        }
    }

    private void viewTransactionDetail() {
        SalesView.showTransactionDetailHeader();
        Order order = findTransaction();
        if (order == null) {
            return;
        }
        SalesView.showBill(order);

        // the bill is the last step before the sale is completed
        if (order.isPending() && !order.isEmpty() && InputHelper.confirmAction("Confirm Sale")) {
            confirmSale(order.getOrderId());
        }
    }

    private void viewTransactionHistory() {
        SalesView.showOrders(salesService.getAllTransactions());
    }

    private void searchTransactionsByCustomer() {
        SalesView.showSearchHeader();
        String keyword = InputHelper.readString("Enter customer ID or name: ");
        SalesView.showOrders(salesService.searchByCustomer(keyword));
    }

    private void cancelTransaction() {
        SalesView.showCancelHeader();
        Order order = findPendingTransaction();
        if (order == null || !InputHelper.confirmAction("Cancel Transaction")) {
            return;
        }
        try {
            salesService.cancelTransaction(order.getOrderId());
            ConsoleView.showSuccess("Sales transaction cancelled successfully.");
        } catch (BusinessException e) {
            ConsoleView.showError("Failed to cancel transaction. " + e.getMessage());
        }
    }

    private void deleteTransaction() {
        SalesView.showDeleteHeader();
        Order order = findTransaction();
        if (order == null || !InputHelper.confirmAction("Delete")) {
            return;
        }
        try {
            salesService.deleteTransaction(order.getOrderId());
            ConsoleView.showSuccess("Sales transaction deleted successfully.");
        } catch (BusinessException e) {
            ConsoleView.showError("Failed to delete transaction. " + e.getMessage());
        }
    }

    private void confirmSale(String orderId) {
        try {
            salesService.confirmSale(orderId);
            ConsoleView.showSuccess("Sale completed successfully.");
        } catch (BusinessException e) {
            ConsoleView.showError("Failed to confirm sale. " + e.getMessage());
        }
    }

    private Order findTransaction() {
        String orderId = InputHelper.readString("Transaction ID: ");
        try {
            return salesService.getTransaction(orderId);
        } catch (BusinessException e) {
            ConsoleView.showError(e.getMessage());
            return null;
        }
    }

    private Order findPendingTransaction() {
        Order order = findTransaction();
        if (order != null && !order.isPending()) {
            ConsoleView.showError("Transaction " + order.getOrderId() + " is " + order.getStatus()
                    + " and cannot be changed.");
            return null;
        }
        return order;
    }

    private void addProductsLoop(String orderId) {
        while (true) {
            String productId = InputHelper.readString("Product ID: ");
            try {
                FoodProduct product = productService.getProduct(productId);
                SalesView.showProductInfo(product);

                int quantity = InputHelper.readIntRange("Quantity: ", 1);

                if (InputHelper.confirmAction("Add")) {
                    salesService.addProduct(orderId, productId, quantity);
                    ConsoleView.showSuccess("Product added to transaction successfully.");
                }
            } catch (BusinessException | IllegalArgumentException e) {
                ConsoleView.showError("Failed to add product. " + e.getMessage());
            }

            if (!InputHelper.confirmAction("Add Another Product")) {
                return;
            }
        }
    }
}
