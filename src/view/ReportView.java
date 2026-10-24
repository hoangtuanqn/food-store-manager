/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.CustomerSpending;
import model.ProductSales;
import model.SalesSummary;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author MSI
 */
public class ReportView {

    private static final String TOP_SPENDING_ROW = "%-13s%-20s%16s%n";
    private static final String BEST_SELLING_ROW = "%-12s%-22s%14s%n";

    public static void showMenu() {
        MenuView.showSubMenu(
                "REPORTS",
                new String[]{
                    "Daily Sales Report",
                    "Monthly Sales Report",
                    "Best-Selling Products",
                    "Highest-Spending Customers"
                }
        );
    }

    public static void showBestSellingProducts(String period, List<ProductSales> products) {
        System.out.println();
        MenuView.showTitle("BEST-SELLING FOOD PRODUCTS");
        System.out.println(period);
        if (products.isEmpty()) {
            ConsoleView.showError("No sales found in this period.");
            return;
        }
        System.out.println();
        System.out.printf(BEST_SELLING_ROW, "Product ID", "Product Name", "Quantity Sold");
        MenuView.showLine();
        products.forEach(p -> System.out.printf(BEST_SELLING_ROW,
                p.getProductId(), p.getProductName(), p.getQuantitySold()));
        MenuView.showLine();
    }

    public static void showTopSpendingCustomers(String period, List<CustomerSpending> customers) {
        System.out.println();
        MenuView.showTitle("HIGHEST-SPENDING CUSTOMERS");
        System.out.println(period);
        if (customers.isEmpty()) {
            ConsoleView.showError("No purchases found in this period.");
            return;
        }
        System.out.println();
        System.out.printf(TOP_SPENDING_ROW, "Customer ID", "Customer Name", "Total Purchase");
        MenuView.showLine();
        customers.forEach(c -> System.out.printf(TOP_SPENDING_ROW,
                c.getCustomerId(), c.getCustomerName(),
                String.format(Locale.US, "%,.0f", c.getTotalPurchase()) + " VND"));
        MenuView.showLine();
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
