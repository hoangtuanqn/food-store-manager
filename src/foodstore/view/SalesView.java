/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.view;

import foodstore.model.FoodProduct;
import foodstore.model.Order;
import foodstore.model.OrderDetail;
import foodstore.model.MembershipType;
import foodstore.util.DateUtil;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author MSI
 */
public class SalesView {

    private static final String BILL_ROW = "%-18s%5s%12s%14s%n";
    private static final String HISTORY_ROW = "%-7s%-20s%-12s%7s%15s  %-10s%n";

    public static void showMenu() {
        MenuView.showSubMenu(
                "SALES MANAGEMENT",
                new String[]{
                    "Create Order",
                    "Add Product to Order",
                    "Remove Product from Order",
                    "View order detail",
                    "View All Orders",
                    "Search orders by customer",
                    "Cancel order",
                    "Delete order"
                }
        );
    }

    public static void showCreateSalesTransactionHeader() {
        MenuView.showTitle("CREATE SALES TRANSACTION");
    }

    public static void showAddProductHeader() {
        MenuView.showTitle("ADD PRODUCT TO TRANSACTION");
    }

    public static void showRemoveProductHeader() {
        MenuView.showTitle("REMOVE PRODUCT FROM TRANSACTION");
    }

    public static void showTransactionDetailHeader() {
        MenuView.showTitle("TRANSACTION DETAIL");
    }

    public static void showSearchHeader() {
        MenuView.showTitle("SEARCH TRANSACTIONS BY CUSTOMER");
    }

    public static void showCancelHeader() {
        MenuView.showTitle("CANCEL TRANSACTION");
    }

    public static void showDeleteHeader() {
        MenuView.showTitle("DELETE TRANSACTION");
    }

    private static String money(double amount) {
        return String.format(Locale.US, "%,.0f", amount);
    }

    public static void showProductInfo(FoodProduct product) {
        System.out.println("Product Name: " + product.getName());
        System.out.println("Available Stock: " + product.getQuantity());
        System.out.println("Expiration Date: " + product.getExpirationDate().format(DateUtil.FORMATTER));
    }

    public static void showBill(Order order) {
        System.out.println();
        MenuView.showTitle("BILL SUMMARY");
        System.out.println();
        System.out.println("Transaction ID: " + order.getOrderId());
        System.out.println("Customer: " + order.getCustomer().getFullName());
        System.out.println("Membership: " + order.getCustomer().getMembershipType());
        System.out.println("Date: " + order.getOrderDate().format(DateUtil.FORMATTER));
        System.out.println("Status: " + order.getStatus());
        System.out.println();

        if (order.isEmpty()) {
            System.out.println("No products in this transaction.");
            return;
        }

        System.out.printf(BILL_ROW, "Product", "Qty", "Price", "Amount");
        MenuView.showLine();
        for (OrderDetail d : order.getDetails()) {
            System.out.printf(BILL_ROW,
                    d.getProduct().getName(),
                    d.getQuantity(),
                    money(d.getUnitPrice()),
                    money(d.getLineTotal()));
        }
        MenuView.showLine();
        System.out.printf("%-30s%19s VND%n", "Subtotal:", money(order.getSubtotal()));
        System.out.printf("%-30s%19s VND%n", discountLabel(order), money(order.getDiscount()));
        MenuView.showLine();
        System.out.printf("%-30s%19s VND%n", "Total Amount:", money(order.getFinalAmount()));
        MenuView.showLine();
    }

    private static String discountLabel(Order order) {
        if (order.getCustomer().getMembershipType() == MembershipType.VIP) {
            return "VIP Discount (10%):";
        }
        return "Discount:";
    }

    public static void showOrders(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            ConsoleView.showError("No sales transactions found.");
            return;
        }
        MenuView.showTitle("TRANSACTION HISTORY");
        System.out.printf(HISTORY_ROW, "ID", "Customer", "Date", "Items", "Total (VND)", "Status");
        MenuView.showLine();
        for (Order o : orders) {
            System.out.printf(HISTORY_ROW,
                    o.getOrderId(),
                    o.getCustomer().getFullName(),
                    o.getOrderDate().format(DateUtil.FORMATTER),
                    o.getTotalQuantity(),
                    money(o.getFinalAmount()),
                    o.getStatus());
        }
        MenuView.showLine();
    }
}
