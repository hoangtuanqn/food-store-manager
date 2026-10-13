/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import controller.CustomerController;
import controller.InventoryController;
import controller.MainController;
import controller.ProductController;
import controller.SalesController;
import repository.CustomerRepository;
import repository.FoodProductRepository;
import repository.OrderRepository;
import service.CustomerService;
import service.InventoryService;
import service.ProductService;
import service.SalesService;

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

        InventoryService inventoryService = new InventoryService(productRepository);
        InventoryController inventoryController = new InventoryController(inventoryService);

        OrderRepository orderRepository = new OrderRepository();
        SalesService salesService = new SalesService(orderRepository, customerRepository, productRepository, inventoryService);
        SalesController salesController = new SalesController(salesService, customerService, productService);
        new MainController(productController, customerController, inventoryController, salesController).run();

    }
}
