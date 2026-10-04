/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.controller;

import foodstore.exception.BusinessException;
import foodstore.model.FoodProduct;
import foodstore.service.ProductService;
import foodstore.util.InputHelper;
import foodstore.view.ConsoleView;
import foodstore.view.MenuView;
import foodstore.view.ProductView;
import java.time.LocalDate;

/**
 *
 * @author MSI
 */
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public void run() {
        while (true) {
            ProductView.showMenu();
            int choice = InputHelper.readIntRange("Choose an option: ", 0, 9);
            switch (choice) {
                case 1:
                    addProduct();
                    break;

                case 0:
                    return;
            }
        }
    }

    private void addProduct() {

        ProductView.showAddHeader();
        String id;
        while (true) {
            id = InputHelper.readString("Product ID: ");
            if(productService.checkIdExist(id)) {
                ConsoleView.showError("Product ID already exists!");
            } else break;
            
        }
        String name = InputHelper.readString("Product Name: ");
        String category = InputHelper.readString("Category: ");
        String unit = InputHelper.readString("Unit: ");
        double price = InputHelper.readPrice("Price: ");
        int quantity = InputHelper.readIntRange("Quantity: ", 1);
        LocalDate production, expiration;
        while (true) {
            production = InputHelper.readDate("Production Date: ");
            expiration = InputHelper.readDate("Expiration Date: ");
            try {
                FoodProduct.requireDates(production, expiration);
                break;
            } catch (IllegalArgumentException e) {
                ConsoleView.showError(e.getMessage() + " Please enter both dates again.");
            }
        }

        FoodProduct product = new FoodProduct(id, name, category, unit, price, quantity, production, expiration);
        try {
            productService.addProduct(product);
            ConsoleView.showSuccess("Food product added successfully.");
        } catch (BusinessException e) {
            ConsoleView.showError(e.getMessage());
        }

    }
}
