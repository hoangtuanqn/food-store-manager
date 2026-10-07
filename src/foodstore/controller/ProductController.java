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
            id = InputHelper.readString("Product ID: ");
            if (productService.checkIdExist(id)) {
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
        String id = InputHelper.readString("Enter ID to update: ");

        FoodProduct exist;
        try {
            exist = productService.getProductById(id);
        } catch (BusinessException e) {
            ConsoleView.showError(e.getMessage());
            return;
        }

        ProductView.showInfo(exist);
        System.out.println("Press Enter to keep the current value.");

        String name = InputHelper.readStringOrDefault("Product Name ", exist.getName());
        String category = InputHelper.readStringOrDefault("Category ", exist.getCategory());
        String unit = InputHelper.readStringOrDefault("Unit ", exist.getUnit());
        double price = InputHelper.readPriceOrDefault("Price ", exist.getPrice());
        int quantity = InputHelper.readIntRangeOrDefault("Quantity ", 0, Integer.MAX_VALUE, exist.getQuantity());

        LocalDate production, expiration;
        while (true) {
            production = InputHelper.readDateOrDefault("Production Date ", exist.getProductionDate());
            expiration = InputHelper.readDateOrDefault("Expiration Date ", exist.getExpirationDate());
            try {
                FoodProduct.requireDates(production, expiration);
                break;
            } catch (IllegalArgumentException e) {
                ConsoleView.showError(e.getMessage() + " Please enter both dates again.");
            }
        }

        try {
            FoodProduct updated = new FoodProduct(exist.getProductId(), name, category, unit, price, quantity, production, expiration);
            productService.updateProduct(updated);
            ConsoleView.showSuccess("Food product updated successfully.");
        } catch (BusinessException | IllegalArgumentException e) {
            ConsoleView.showError(e.getMessage());
        }
    }

    private void deleteProduct() {
        ProductView.showDeleteHeader();
        String Id = InputHelper.readString("Enter Product ID to delete: ");

        FoodProduct existing;
        try {
            existing = productService.getProductById(Id);
        } catch (BusinessException e) {
            ConsoleView.showError(e.getMessage());
            return;
        }
        
        ProductView.showInfo(existing);

        int confirm = InputHelper.readIntRange("1.Delete  2.Cancel: ", 1, 2);
        if (confirm == 2) {
            ConsoleView.showSuccess("Delete cancelled.");
            return;
        }

        try {
            productService.deleteProduct(Id);
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
