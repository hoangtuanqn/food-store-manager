/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import exception.BusinessException;
import model.FoodProduct;
import model.Order;
import model.ReturnOrder;
import service.CustomerService;
import service.ProductService;
import service.ReturnService;
import service.SalesService;
import helper.InputHelper;
import view.ConsoleView;
import view.SalesView;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MSI
 */
public class SalesController {

    private final SalesService salesService;
    private final CustomerService customerService;
    private final ProductService productService;
    private final ReturnService returnService;

    public SalesController(SalesService salesService, CustomerService customerService,
            ProductService productService, ReturnService returnService) {
        this.salesService = salesService;
        this.customerService = customerService;
        this.productService = productService;
        this.returnService = returnService;
    }

    public void run() {
        while (true) {
            SalesView.showMenu();

            int choice = InputHelper.readIntRange(
                    "Choose an option: ", 0, 9
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
                case 9:
                    returnProducts();
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

    private void returnProducts() {
        SalesView.showReturnHeader();
        Order order = findTransaction();
        if (order == null) {
            return;
        }
        if (!order.isCompleted()) {
            ConsoleView.showError("Only completed transactions can be returned.");
            return;
        }
        SalesView.showBill(order);

        List<ReturnService.Line> lines = new ArrayList<>();
        while (true) {
            String productId = InputHelper.readId("Product ID: ");
            try {
                int chosen = lines.stream().filter(l -> l.getProductId().equals(productId)).mapToInt(ReturnService.Line::getQuantity).sum();
                int returnable = returnService.getReturnableQuantity(order.getOrderId(), productId) - chosen;
                if (returnable <= 0) {
                    ConsoleView.showError("Nothing left to return for this product.");
                } else {
                    int quantity = InputHelper.readIntRange("Quantity (max " + returnable + "): ", 1, returnable);
                    boolean restock = InputHelper.readIntRange("Return to stock? [1] Yes [2] No: ", 1, 2) == 1;
                    lines.add(new ReturnService.Line(productId, quantity, restock));
                }
            } catch (BusinessException e) {
                ConsoleView.showError("Failed to add product. " + e.getMessage());
            }
            if (!InputHelper.confirmAction("Add Another Product")) {
                break;
            }
        }
        if (lines.isEmpty()) {
            return;
        }

        String reason = InputHelper.readString("Reason: ");
        if (!InputHelper.confirmAction("Return")) {
            return;
        }
        try {
            ReturnOrder returnOrder = returnService.createReturn(order.getOrderId(), reason, lines);
            SalesView.showReturn(returnOrder);
            ConsoleView.showSuccess("Return " + returnOrder.getReturnId() + " created successfully.");
        } catch (BusinessException | IllegalArgumentException e) {
            ConsoleView.showError("Failed to return products. " + e.getMessage());
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
