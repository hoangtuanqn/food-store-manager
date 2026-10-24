/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.SalesSummary;
import java.util.Locale;

/**
 *
 * @author MSI
 */
public class ReportView {

    public static void showMenu() {
        MenuView.showSubMenu(
                "REPORTS",
                new String[]{
                    "Daily Sales Report",
                    "Monthly Sales Report"
                }
        );
    }

    public static void showSalesReport(String title, String period, SalesSummary summary) {
        System.out.println();
        MenuView.showTitle(title);
        System.out.println(period);
        System.out.println();
        System.out.println("Total Transactions: " + summary.getTotalTransactions());
        System.out.println("Total Food Products Sold: " + summary.getTotalProductsSold());
        System.out.println("Total Revenue: " + String.format(Locale.US, "%,.0f", summary.getTotalRevenue()) + " VND");
        MenuView.showLine();
    }
}
