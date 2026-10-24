/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import model.CustomerSpending;
import model.Order;
import model.ProductSales;
import model.ReturnDetail;
import model.ReturnOrder;
import model.SalesSummary;
import repository.ReturnRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author MSI
 */
public class ReportService {

    private static final int BEST_SELLING_LIMIT = 10;
    private static final int TOP_SPENDING_LIMIT = 10;

    private final SalesService salesService;
    private final ReturnRepository returnRepository;

    public ReportService(SalesService salesService, ReturnRepository returnRepository) {
        this.salesService = salesService;
        this.returnRepository = returnRepository;
    }

    public SalesSummary getDailySummary(LocalDate day) {
        return getSummary(day, day);
    }

    public SalesSummary getMonthlySummary(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return getSummary(yearMonth.atDay(1), yearMonth.atEndOfMonth());
    }

    public List<ProductSales> getBestSellingProducts(LocalDate from, LocalDate to) {
        Map<String, Integer> quantities = new HashMap<>();
        Map<String, String> names = new HashMap<>();
        getCompletedOrders(from, to).stream()
                .flatMap(order -> order.getDetails().stream())
                .forEach(d -> {
                    quantities.merge(d.getProductId(), d.getQuantity(), Integer::sum);
                    names.put(d.getProductId(), d.getProductName());
                });
        getReturns(from, to).stream()
                .flatMap(returnOrder -> returnOrder.getDetails().stream())
                .forEach(d -> {
                    quantities.merge(d.getProductId(), -d.getQuantity(), Integer::sum);
                    names.putIfAbsent(d.getProductId(), d.getProductName());
                });

        return quantities.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .map(entry -> new ProductSales(entry.getKey(), names.get(entry.getKey()), entry.getValue()))
                .sorted(Comparator.comparingInt(ProductSales::getQuantitySold).reversed()
                        .thenComparing(ProductSales::getProductId))
                .limit(BEST_SELLING_LIMIT)
                .collect(Collectors.toList());
    }

    public List<CustomerSpending> getTopSpendingCustomers(LocalDate from, LocalDate to) {
        Map<String, Double> totals = new HashMap<>();
        Map<String, String> names = new HashMap<>();
        getCompletedOrders(from, to).forEach(order -> {
            totals.merge(order.getCustomerId(), order.getFinalAmount(), Double::sum);
            names.put(order.getCustomerId(), order.getCustomerName());
        });
        getReturns(from, to).forEach(returnOrder -> {
            Order order = salesService.getTransaction(returnOrder.getOrderId());
            totals.merge(order.getCustomerId(), -returnOrder.getRefundAmount(), Double::sum);
            names.putIfAbsent(order.getCustomerId(), order.getCustomerName());
        });

        return totals.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .map(entry -> new CustomerSpending(entry.getKey(), names.get(entry.getKey()), entry.getValue()))
                .sorted(Comparator.comparingDouble(CustomerSpending::getTotalPurchase).reversed()
                        .thenComparing(CustomerSpending::getCustomerId))
                .limit(TOP_SPENDING_LIMIT)
                .collect(Collectors.toList());
    }

    private List<Order> getCompletedOrders(LocalDate from, LocalDate to) {
        return salesService.getAllTransactions().stream()
                .filter(Order::isCompleted)
                .filter(order -> isWithin(order.getOrderDate(), from, to))
                .collect(Collectors.toList());
    }

    private List<ReturnOrder> getReturns(LocalDate from, LocalDate to) {
        return returnRepository.findAll().stream()
                .filter(returnOrder -> isWithin(returnOrder.getReturnDate(), from, to))
                .collect(Collectors.toList());
    }

    private SalesSummary getSummary(LocalDate from, LocalDate to) {
        List<Order> orders = getCompletedOrders(from, to);
        List<ReturnOrder> returns = getReturns(from, to);

        int productsSold = orders.stream().mapToInt(Order::getTotalQuantity).sum()
                - returns.stream().flatMap(r -> r.getDetails().stream()).mapToInt(ReturnDetail::getQuantity).sum();
        double revenue = orders.stream().mapToDouble(Order::getFinalAmount).sum()
                - returns.stream().mapToDouble(ReturnOrder::getRefundAmount).sum();
        return new SalesSummary(orders.size(), productsSold, revenue);
    }

    private static boolean isWithin(LocalDate date, LocalDate from, LocalDate to) {
        return !date.isBefore(from) && !date.isAfter(to);
    }
}
