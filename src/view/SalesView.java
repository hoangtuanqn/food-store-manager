/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.FoodProduct;
import model.Order;
import model.ReturnOrder;
import model.MembershipType;
import helper.DateHelper;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author MSI
 */
public class SalesView {

    private static final String BILL_ROW = "%-18s%5s%12s%14s%n";
    private static final String RETURN_ROW = "%-18s%5s%14s%12s%n";
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
                    "Delete order",
                    "Return products"
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

    public static void showReturnHeader() {
        MenuView.showTitle("RETURN PRODUCTS");
    }

    public static void showReturn(ReturnOrder returnOrder) {
        System.out.println();
        MenuView.showTitle("RETURN " + returnOrder.getReturnId());
        System.out.println("Transaction ID: " + returnOrder.getOrderId());
        System.out.println("Date: " + returnOrder.getReturnDate().format(DateHelper.FORMATTER));
        System.out.println("Reason: " + returnOrder.getReason());
        System.out.println();
        System.out.printf(RETURN_ROW, "Product", "Qty", "Amount", "Restocked");
        MenuView.showLine();
        returnOrder.getDetails().forEach(detail -> System.out.printf(RETURN_ROW,
                detail.getProductName(),
                detail.getQuantity(),
                money(detail.getLineTotal()),
                detail.isRestocked() ? "Yes" : "No"));
        MenuView.showLine();
        System.out.printf("%-30s%19s VND%n", "Refund Amount:", money(returnOrder.getRefundAmount()));
        MenuView.showLine();
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
        System.out.println("Expiration Date: " + product.getExpirationDate().format(DateHelper.FORMATTER));
    }

    public static void showBill(Order order) {
        System.out.println();
        MenuView.showTitle("BILL SUMMARY");
        System.out.println();
        System.out.println("Transaction ID: " + order.getOrderId());
        System.out.println("Customer: " + order.getCustomerName());
        System.out.println("Membership: " + order.getMembershipType());
        System.out.println("Date: " + order.getOrderDate().format(DateHelper.FORMATTER));
        System.out.println("Status: " + order.getStatus());
        System.out.println();

        if (order.isEmpty()) {
            System.out.println("No products in this transaction.");
            return;
        }

        System.out.printf(BILL_ROW, "Product", "Qty", "Price", "Amount");
        MenuView.showLine();
        order.getDetails().forEach(detail -> System.out.printf(BILL_ROW,
                detail.getProductName(),
                detail.getQuantity(),
                money(detail.getUnitPrice()),
                money(detail.getLineTotal())));
        MenuView.showLine();
        System.out.printf("%-30s%19s VND%n", "Subtotal:", money(order.getSubtotal()));
        System.out.printf("%-30s%19s VND%n", discountLabel(order), money(order.getDiscount()));
        MenuView.showLine();
        System.out.printf("%-30s%19s VND%n", "Total Amount:", money(order.getFinalAmount()));
        MenuView.showLine();
    }

    private static String discountLabel(Order order) {
        if (order.getMembershipType() == MembershipType.VIP) {
            return String.format("VIP Discount (%.0f%%):", order.getDiscountRate() * 100);
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
        orders.forEach(order -> System.out.printf(HISTORY_ROW,
                order.getOrderId(),
                order.getCustomerName(),
                order.getOrderDate().format(DateHelper.FORMATTER),
                order.getTotalQuantity(),
                money(order.getFinalAmount()),
                order.getStatus()));
        MenuView.showLine();
    }
}
