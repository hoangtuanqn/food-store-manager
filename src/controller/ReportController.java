/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import helper.DateHelper;
import helper.InputHelper;
import service.ReportService;
import view.ConsoleView;
import view.ReportView;
import java.time.LocalDate;

/**
 *
 * @author MSI
 */
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    public void run() {
        while (true) {
            ReportView.showMenu();
            int choice = InputHelper.readIntRange("Choose an option: ", 0, 6);
            switch (choice) {
                case 1:
                    showDailySalesReport();
                    break;
                case 2:
                    showMonthlySalesReport();
                    break;
                case 3:
                    showBestSellingProducts();
                    break;
                case 4:
                    showTopSpendingCustomers();
                    break;
                case 5:
                    ReportView.showLowStockReport(reportService.getLowStockProducts());
                    break;
                case 6:
                    ReportView.showExpiredReport(reportService.getExpiredProducts());
                    break;
                case 0:
                    return;
            }
        }
    }

    private void showDailySalesReport() {
        LocalDate day = InputHelper.readDate("Date: ");
        ReportView.showSalesReport("DAILY SALES REPORT", "Date: " + DateHelper.format(day),
                reportService.getDailySummary(day));
    }

    private void showBestSellingProducts() {
        LocalDate[] period = readPeriod();
        ReportView.showBestSellingProducts(describePeriod(period),
                reportService.getBestSellingProducts(period[0], period[1]));
    }

    private void showTopSpendingCustomers() {
        LocalDate[] period = readPeriod();
        ReportView.showTopSpendingCustomers(describePeriod(period),
                reportService.getTopSpendingCustomers(period[0], period[1]));
    }

    private LocalDate[] readPeriod() {
        LocalDate from = InputHelper.readDate("From date: ");
        LocalDate to = InputHelper.readDate("To date: ");
        while (to.isBefore(from)) {
            ConsoleView.showError("To date cannot be before from date.");
            to = InputHelper.readDate("To date: ");
        }
        return new LocalDate[]{from, to};
    }

    private String describePeriod(LocalDate[] period) {
        return "From " + DateHelper.format(period[0]) + " to " + DateHelper.format(period[1]);
    }

    private void showMonthlySalesReport() {
        int month = InputHelper.readIntRange("Month (1-12): ", 1, 12);
        int year = InputHelper.readIntRange("Year: ", 2000, 2100);
        ReportView.showSalesReport("MONTHLY SALES REPORT", "Month / Year: " + month + " / " + year,
                reportService.getMonthlySummary(year, month));
    }
}
