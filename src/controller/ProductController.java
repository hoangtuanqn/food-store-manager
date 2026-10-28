/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import exception.BusinessException;
import model.FoodProduct;
import service.ProductService;
import helper.InputHelper;
import view.ConsoleView;
import view.ProductView;
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
            int choice = InputHelper.readIntRange("Choose an option: ", 0, 6);
            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    updateProduct();
                    break;
                case 3:
                    deleteProduct();
                    break;
                case 4:
                    viewAllProducts();
                    break;
                case 5:
                    searchKeyWord();
                    break;
                case 6:
                    searchByExpirationDate();
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
            id = InputHelper.readId("Product ID: ");
            if (productService.existsById(id)) {
                ConsoleView.showError("Product ID already exists!");
            } else {
                break;
            }
        }

        String name = InputHelper.readString("Product Name: ");
        String category = InputHelper.readString("Category: ");
        String unit = InputHelper.readString("Unit: ");
        double price = InputHelper.readPrice("Price: ");
        int quantity = InputHelper.readIntRange("Quantity: ", 0);

        LocalDate production;
        while (true) {
            production = InputHelper.readDate("Production Date: ");
            try {
                FoodProduct.requireProductionDate(production);
                break;
            } catch (IllegalArgumentException e) {
                ConsoleView.showError(e.getMessage());
            }
        }

        LocalDate expiration;
        while (true) {
            expiration = InputHelper.readDate("Expiration Date: ");
            try {
                FoodProduct.requireExpirationDate(production, expiration);
                break;
            } catch (IllegalArgumentException e) {
                ConsoleView.showError(e.getMessage());
            }
        }

        if (!InputHelper.confirmAction("Add")) {
            return;
        }

        try {
            FoodProduct product = new FoodProduct(id, name, category, unit, price, quantity, production, expiration);
            productService.addProduct(product);
            ConsoleView.showSuccess("Food product added successfully.");
        } catch (BusinessException | IllegalArgumentException e) {
            ConsoleView.showError(e.getMessage());
        }
    }

    private void updateProduct() {
        ProductView.showUpdateHeader();
        String id = InputHelper.readId("Enter ID to update: ");

        FoodProduct existingProduct;
        try {
            existingProduct = productService.getProductById(id);
        } catch (BusinessException e) {
            ConsoleView.showError(e.getMessage());
            return;
        }

        ProductView.showInfo(existingProduct);
        System.out.println("Press Enter to keep the current value.");

        String name = InputHelper.readStringOrDefault("Product Name ", existingProduct.getName());
        String category = InputHelper.readStringOrDefault("Category ", existingProduct.getCategory());
        String unit = InputHelper.readStringOrDefault("Unit ", existingProduct.getUnit());
        double price = InputHelper.readPriceOrDefault("Price ", existingProduct.getPrice());
        int quantity = InputHelper.readIntRangeOrDefault("Quantity ", 0, Integer.MAX_VALUE, existingProduct.getQuantity());

        LocalDate production;
        while (true) {
            production = InputHelper.readDateOrDefault("Production Date ", existingProduct.getProductionDate());
            try {
                FoodProduct.requireProductionDate(production);
                break;
            } catch (IllegalArgumentException e) {
                ConsoleView.showError(e.getMessage());
            }
        }

        LocalDate expiration;
        while (true) {
            expiration = InputHelper.readDateOrDefault("Expiration Date ", existingProduct.getExpirationDate());
            try {
                FoodProduct.requireExpirationDate(production, expiration);
                break;
            } catch (IllegalArgumentException e) {
                ConsoleView.showError(e.getMessage());
            }
        }

        if (!InputHelper.confirmAction("Update")) {
            return;
        }

        try {
            FoodProduct updated = new FoodProduct(existingProduct.getProductId(), name, category, unit, price, quantity, production, expiration);
            productService.updateProduct(updated);
            ConsoleView.showSuccess("Food product updated successfully.");
        } catch (BusinessException | IllegalArgumentException e) {
            ConsoleView.showError(e.getMessage());
        }
    }

    private void deleteProduct() {
        ProductView.showDeleteHeader();
        String id = InputHelper.readId("Enter Product ID to delete: ");

        FoodProduct existing;
        try {
            existing = productService.getProductById(id);
        } catch (BusinessException e) {
            ConsoleView.showError(e.getMessage());
            return;
        }

        ProductView.showInfo(existing);

        if (!InputHelper.confirmAction("Delete")) {
            return;
        }

        try {
            productService.deleteProduct(id);
            ConsoleView.showSuccess("Food product deleted successfully.");
        } catch (BusinessException e) {
            ConsoleView.showError(e.getMessage());
        }
    }

    private void viewAllProducts() {
        ProductView.showViewAllHeader();
        ProductView.showTable(productService.getAllProducts());
    }

    private void searchKeyWord() {
        ProductView.showSearchHeader();
        String keyWords = InputHelper.readString("Enter keyword: ");
        ProductView.showTable(productService.searchKeyWords(keyWords));
    }

    private void searchByExpirationDate() {
        ProductView.showSearchHeader();
        LocalDate date = InputHelper.readDate("Enter expiration date: ");
        ProductView.showTable(productService.searchExpirationDate(date));
    }
}
