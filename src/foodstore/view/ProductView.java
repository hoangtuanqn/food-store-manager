/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.view;

import foodstore.model.FoodProduct;
import java.util.List;

public class ProductView {

    private static final String ROW_FORMAT = "%-6s%-18s%-12s%-10s%-9s%-6s%n";

    public static void showMenu() {
        String[] options = {
            "Add Food Product",
            "Update Food Product",
            "Remove Food Product",
            "View All Food Products",
            "Search by Name or Category",
            "Search by Expiration Date",
            "View Available Products",
            "View Expired Products",
            "View Low Stock Products"
        };
        MenuView.showSubMenu("MANAGE FOOD PRODUCTS", options);
    }

    public static void showAddHeader() {
        MenuView.showTitle("ADD FOOD PRODUCT");
    }

    public static void showInventoryMenu() {
        String[] options = {
            "View Low Stock Products",
            "View Expired Products",
            "View Near Expiry Products",
            "View Sellable Products"
        };
        MenuView.showSubMenu("INVENTORY MANAGEMENT", options);
    }

    public static void showLowStockProducts(List<FoodProduct> products) {
        showProductTable("LOW STOCK PRODUCTS", products, "No low stock products.");
    }

    public static void showExpiredProducts(List<FoodProduct> products) {
        showProductTable("EXPIRED PRODUCTS", products, "No expired products.");
    }

    public static void showNearExpiryProducts(List<FoodProduct> products) {
        showProductTable("NEAR EXPIRY PRODUCTS", products, "No near expiry products.");
    }

    public static void showSellableProducts(List<FoodProduct> products) {
        showProductTable("SELLABLE PRODUCTS", products, "No sellable products.");
    }

    public static void showError(String message) {
        System.out.println("Error: " + message);
    }

    private static void showProductTable(String title, List<FoodProduct> products, String emptyMessage) {
        if (products == null || products.isEmpty()) {
            ConsoleView.showError(emptyMessage);
            return;
        }
        MenuView.showTitle(title);
        System.out.printf(ROW_FORMAT, "ID", "Name", "Category", "Unit", "Price", "Stock");
        MenuView.showLine();
        for (FoodProduct p : products) {
            System.out.printf(ROW_FORMAT,
                    p.getProductId(),
                    p.getName(),
                    p.getCategory(),
                    p.getUnit(),
                    p.getPrice(),
                    p.getQuantity());
        }
        MenuView.showLine();
    }
}
