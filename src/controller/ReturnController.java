/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import exception.BusinessException;
import helper.InputHelper;
import model.Order;
import model.ReturnOrder;
import service.ReturnService;
import service.SalesService;
import view.ConsoleView;
import view.SalesView;
import java.util.ArrayList;
import java.util.List;

public class ReturnController {

    private final SalesService salesService;
    private final ReturnService returnService;

    public ReturnController(SalesService salesService, ReturnService returnService) {
        this.salesService = salesService;
        this.returnService = returnService;
    }

    public void run() {
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

        List<ReturnService.Line> lines = readReturnLines(order);
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

    private List<ReturnService.Line> readReturnLines(Order order) {
        List<ReturnService.Line> lines = new ArrayList<>();
        while (true) {
            String productId = InputHelper.readId("Product ID: ");
            try {
                int chosen = lines.stream().filter(line -> line.getProductId().equals(productId)).mapToInt(ReturnService.Line::getQuantity).sum();
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
                return lines;
            }
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
}
