/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.view;

/**
 *
 * @author MSI
 */
public class MenuView {
    
    private static final int WIDTH = 45;

    public static void showMainMenu() {
        printLine('=');
        printTitleCenter("FOOD SALES MANAGEMENT SYSTEM");
        printLine('=');
        String[] menuList = {
            "Manage Food Products",
            "Manage Customers",
            "Sales Management",
            "Inventory Management",
            "Reports",
            "Exit"
        };
        for (int i = 0; i < menuList.length; ++i) {
            System.out.println((i + 1) + ". " + menuList[i]);
        }
        System.out.println("Choose an option: ");
    }

    public static void showTitle(String title) {
        System.out.println("----------- " + title + " -----------");
    }

    public static void showSuccess(String message) {
        System.out.println("[SUCCESS] " + message);
    }

    public static void showError(String message) {
        System.out.println("[ERROR] " + message);
    }
    
    private static void printLine(char c) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < WIDTH; ++i) {
            sb.append(c);
        }
        System.out.println(sb.toString());
    }
    
    private static void printTitleCenter(String title) {
        int padding = Math.max(0, (WIDTH - title.length()) / 2);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < padding; ++i) {
            sb.append(' ');
        }
        System.out.println(sb.toString() + title + sb.toString());
    }
}
