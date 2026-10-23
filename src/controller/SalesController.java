/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import exception.BusinessException;
import model.FoodProduct;
import model.Order;
import service.CustomerService;
import service.ProductService;
import service.SalesService;
import helper.InputHelper;
import view.ConsoleView;
import view.SalesView;
import java.time.LocalDate;

/**
 *
 * @author MSI
 */
public class SalesController {

    private final SalesService salesService;
    private final CustomerService customerService;
    private final ProductService productService;

    // Giữ lại constructor đầy đủ, xóa bỏ constructor thừa gây lỗi UnsupportedOperationException
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
            // Sử dụng readId để chuẩn hóa ID không phân biệt hoa thường (Task 12)
            id = InputHelper.readId("Transaction ID: ");

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
            // Sử dụng readId để chuẩn hóa Customer ID
            customerId = InputHelper.readId("Customer ID: ");

            if (!customerService.checkIdExist(customerId)) {
                ConsoleView.showError(
                        "Failed to create transaction. Customer ID does not exist."
                );
            } else {
                break;
            }
        }

        LocalDate date;
        while (true) {
            date = InputHelper.readDate("Date: ");
            try {
                salesService.validateTransactionDate(date);
                break;
            } catch (BusinessException e) {
                ConsoleView.showError(e.getMessage() + " Please enter the date again.");
            }
        }

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

        // Dùng readId để chuẩn hóa Product ID khi xóa sản phẩm
        String productId = InputHelper.readId("Product ID: ");
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
        // Dùng readId để chuẩn hóa Transaction ID khi tìm kiếm
        String orderId = InputHelper.readId("Transaction ID: ");
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
            // Dùng readId để chuẩn hóa Product ID khi thêm sản phẩm vào hóa đơn
            String productId = InputHelper.readId("Product ID: ");
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