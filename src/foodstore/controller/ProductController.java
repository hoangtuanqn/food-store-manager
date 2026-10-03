/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.controller;

import foodstore.execption.BusinessException;
import foodstore.model.FoodProduct;
import foodstore.service.ProductService;
import foodstore.util.InputHelper;
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
    
    private void addProduct() {
        
        ProductView.showAddHeader();
        
        String id = InputHelper.readString("Product ID: ");
        String name = InputHelper.readString("Product Name: ");
        String category = InputHelper.readString("Category: ");
        String unit = InputHelper.readString("Unit: ");
        double price = InputHelper.readDouble("Price: ");
        int quantity = InputHelper.readInt("Quantity: ");
        LocalDate production = InputHelper.readDate("Production Date: ");
        LocalDate expiration = InputHelper.readDate("Expiration Date: ");
        
        FoodProduct product = new FoodProduct(id, name, category, unit, price, quantity, production, expiration);
        try {
            productService.addProduct(product);
            System.out.println("Food product added successfully.");
        } catch(BusinessException e){
            System.err.println(e.getMessage());
        }
        
    }
}
