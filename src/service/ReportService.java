/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import model.Order;
import model.ReturnDetail;
import model.ReturnOrder;
import model.SalesSummary;
import repository.ReturnRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author MSI
 */
public class ReportService {

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
