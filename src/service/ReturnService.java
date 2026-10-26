/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import exception.InvalidQuantityException;
import exception.InvalidTransactionException;
import exception.NotFoundException;
import model.Order;
import model.OrderDetail;
import model.ReturnDetail;
import model.ReturnOrder;
import repository.ReturnRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ad
 */
public class ReturnService {

    public static class Line {

        private final String productId;
        private final int quantity;
        private final boolean restock;

        public Line(String productId, int quantity, boolean restock) {
            this.productId = productId;
            this.quantity = quantity;
            this.restock = restock;
        }

        public String getProductId() {
            return productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public boolean isRestock() {
            return restock;
        }
    }

    private final SalesService salesService;
    private final ReturnRepository returnRepository;
    private final InventoryService inventoryService;

    public ReturnService(SalesService salesService, ReturnRepository returnRepository, InventoryService inventoryService) {
        this.salesService = salesService;
        this.returnRepository = returnRepository;
        this.inventoryService = inventoryService;
    }

    public int getReturnableQuantity(String orderId, String productId) {
        Order order = salesService.getTransaction(orderId);
        OrderDetail detail = order.findDetail(productId);
        if (detail == null) {
            throw new NotFoundException("Product ID " + productId + " is not in this transaction.");
        }
        return detail.getQuantity() - returnRepository.countReturned(orderId, productId);
    }

    public List<ReturnOrder> getReturns(String orderId) {
        return returnRepository.findByOrderId(orderId);
    }

    public ReturnOrder createReturn(String orderId, String reason, List<Line> lines) {
        Order order = salesService.getTransaction(orderId);
        if (!order.isCompleted()) {
            throw new InvalidTransactionException("Only completed transactions can be returned.");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new InvalidTransactionException("Return reason is required.");
        }
        if (lines == null || lines.isEmpty()) {
            throw new InvalidTransactionException("A return must contain at least one product.");
        }

        Map<String, Integer> requested = new HashMap<>();
        List<ReturnDetail> details = new ArrayList<>();
        for (Line line : lines) {
            if (line.getQuantity() <= 0) {
                throw new InvalidQuantityException("Quantity must be greater than zero.");
            }
            int alreadyRequested = requested.getOrDefault(line.getProductId(), 0);
            int returnable = getReturnableQuantity(orderId, line.getProductId()) - alreadyRequested;
            if (line.getQuantity() > returnable) {
                throw new InvalidQuantityException("Cannot return more than purchased: " + returnable
                        + " left to return for product " + line.getProductId() + ".");
            }
            requested.put(line.getProductId(), alreadyRequested + line.getQuantity());
            details.add(new ReturnDetail(order.findDetail(line.getProductId()), line.getQuantity(), line.isRestock()));
        }

        ReturnOrder returnOrder = new ReturnOrder(returnRepository.nextId(), orderId, reason,
                LocalDate.now(), order.getDiscountRate(), details);
        returnRepository.add(returnOrder);
        details.stream()
                .filter(ReturnDetail::isRestocked)
                .forEach(detail -> inventoryService.restockFromReturn(detail.getProductId(), detail.getQuantity(), orderId));
        return returnOrder;
    }
}
