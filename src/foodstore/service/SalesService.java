/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.service;

import foodstore.exception.BusinessException;
import foodstore.exception.DuplicateIdException;
import foodstore.exception.InvalidQuantityException;
import foodstore.exception.InvalidTransactionException;
import foodstore.exception.NotFoundException;
import foodstore.model.Customer;
import foodstore.model.FoodProduct;
import foodstore.model.Order;
import foodstore.model.OrderDetail;
import foodstore.model.OrderStatus;
import foodstore.repository.CustomerRepository;
import foodstore.repository.FoodProductRepository;
import foodstore.repository.OrderRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MSI
 */
public class SalesService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final FoodProductRepository productRepository;
    private final InventoryService inventoryService;

    public SalesService(OrderRepository orderRepository, CustomerRepository customerRepository,
            FoodProductRepository productRepository, InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
    }

    public boolean checkIdExist(String id) {
        return orderRepository.existsById(id);
    }

    public Order getTransaction(String orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Transaction ID " + orderId + " does not exist."));
    }

    public Order createTransaction(String orderId, String customerId, LocalDate date) {
        if (orderRepository.existsById(orderId)) {
            throw new DuplicateIdException("Transaction ID already exists.");
        }
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer ID " + customerId + " does not exist."));

        Order order = new Order(orderId, customer, date);
        orderRepository.add(order);
        return order;
    }

    public void addProduct(String orderId, String productId, int quantity) {
        Order order = getTransaction(orderId);
        FoodProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product ID " + productId + " does not exist."));
        if (!order.isPending()) {
            throw new InvalidTransactionException("Transaction " + orderId + " is " + order.getStatus() + " and cannot be changed.");
        }
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero.");
        }

        // the same product can be added several times, so check the accumulated quantity
        OrderDetail existing = order.findDetail(productId);
        int totalQuantity = quantity + (existing == null ? 0 : existing.getQuantity());
        inventoryService.checkSellable(productId, totalQuantity);

        order.addDetail(new OrderDetail(product, quantity));
        orderRepository.update(order);
    }

    public void removeProduct(String orderId, String productId) {
        Order order = getTransaction(orderId);
        order.removeDetail(productId);
        orderRepository.update(order);
    }

    public Order confirmSale(String orderId) {
        Order order = getTransaction(orderId);
        if (!order.isPending()) {
            throw new InvalidTransactionException("Transaction " + orderId + " is " + order.getStatus() + " and cannot be changed.");
        }
        if (order.isEmpty()) {
            throw new InvalidTransactionException("A sales transaction must contain at least one product.");
        }

        // stock and expiry may have changed since the product was added: check every line first
        for (OrderDetail d : order.getDetails()) {
            try {
                inventoryService.checkSellable(d.getProduct().getProductId(), d.getQuantity());
            } catch (BusinessException e) {
                throw new InvalidTransactionException(d.getProduct().getName() + ": " + e.getMessage());
            }
        }
        for (OrderDetail d : order.getDetails()) {
            inventoryService.deductInventory(d.getProduct().getProductId(), d.getQuantity());
        }
        order.complete();
        orderRepository.update(order);
        return order;
    }

    public void cancelTransaction(String orderId) {
        Order order = getTransaction(orderId);
        order.cancel();
        orderRepository.update(order);
    }

    public void deleteTransaction(String orderId) {
        Order order = getTransaction(orderId);
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new InvalidTransactionException("Completed transactions cannot be deleted.");
        }
        orderRepository.deleteById(orderId);
    }

    public List<Order> getAllTransactions() {
        return orderRepository.findAll();
    }

    public List<Order> searchByCustomer(String keyword) {
        List<Order> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return result;
        }
        String lowerKeyword = keyword.trim().toLowerCase();
        for (Order order : orderRepository.findAll()) {
            Customer c = order.getCustomer();
            if (c.getCustomerId().toLowerCase().equals(lowerKeyword)
                    || c.getFullName().toLowerCase().contains(lowerKeyword)) {
                result.add(order);
            }
        }
        return result;
    }
}
