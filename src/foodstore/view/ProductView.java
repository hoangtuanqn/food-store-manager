/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.view;

/**
 *
 * @author MSI
 */
public class ProductView {
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
}
