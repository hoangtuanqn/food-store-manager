/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore;

import foodstore.controller.CustomerController;
import foodstore.controller.MainController;
import foodstore.controller.ProductController;
import foodstore.repository.CustomerRepository;
import foodstore.repository.FoodProductRepository;
import foodstore.service.CustomerService;
import foodstore.service.ProductService;

/**
 *
 * @author MSI
 */
public class Main {

    public static void main(String[] args) {
        FoodProductRepository productRepository = new FoodProductRepository();
        ProductService productService = new ProductService(productRepository);
        ProductController productController = new ProductController(productService);
        
        CustomerRepository customerRepository = new CustomerRepository();
        CustomerService customerService = new CustomerService(customerRepository);
        CustomerController customerController = new CustomerController(customerService);
        
        new MainController(productController, customerController).run();

    }
}
