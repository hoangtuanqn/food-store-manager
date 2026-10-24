/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import helper.DateHelper;
import helper.InputHelper;
import service.ReportService;
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
            int choice = InputHelper.readIntRange("Choose an option: ", 0, 2);
            switch (choice) {
                case 1:
                    showDailySalesReport();
                    break;
                case 2:
                    showMonthlySalesReport();
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

    private void showMonthlySalesReport() {
        int month = InputHelper.readIntRange("Month (1-12): ", 1, 12);
        int year = InputHelper.readIntRange("Year: ", 2000, 2100);
        ReportView.showSalesReport("MONTHLY SALES REPORT", "Month / Year: " + month + " / " + year,
                reportService.getMonthlySummary(year, month));
    }
}
