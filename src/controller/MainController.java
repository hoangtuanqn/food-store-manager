/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import helper.InputHelper;
import view.ConsoleView;
import view.MenuView;

/**
 *
 * @author ad
 */
public class MainController {

    private final ProductController productController;
    private final CustomerController customerController;
    private final InventoryController inventoryController;
    private final SalesController salesController;
    private final ReportController reportController;

    public MainController(
            ProductController productController, 
            CustomerController customerController, 
            InventoryController inventoryController, 
            SalesController salesController,
            ReportController reportController) {
        this.productController = productController;
        this.customerController = customerController;
        this.inventoryController = inventoryController;
        this.salesController = salesController;
        this.reportController = reportController;
    }

    public void run() {
        while (true) {
            MenuView.showMainMenu();
            int choice = InputHelper.readIntRange("Choose an option: ", 0, 5);

            switch (choice) {
                case 1:
                    productController.run();
                    break;
                case 2:
                    customerController.run();
                    break;
                case 3:
                    salesController.run();
                    break;
                case 4:
                    inventoryController.run();
                    break;
                case 5:
                    reportController.run();
                    break;
                case 0:
                    ConsoleView.showSuccess("Successfully exited the program!");
                    return;
            }
        }
    }
}
