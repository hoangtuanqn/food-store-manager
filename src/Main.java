/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import controller.CustomerController;
import controller.InventoryController;
import controller.MainController;
import controller.ProductController;
import controller.ReportController;
import controller.SalesController;
import repository.CustomerRepository;
import repository.FoodProductRepository;
import repository.OrderRepository;
import repository.ReturnRepository;
import repository.StockMovementRepository;
import service.CustomerService;
import service.InventoryService;
import service.ProductService;
import service.ReportService;
import service.ReturnService;
import service.SalesService;

/**
 *
 * @author MSI
 */
public class Main {

    public static void main(String[] args) {
        FoodProductRepository productRepository = new FoodProductRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        OrderRepository orderRepository = new OrderRepository();

        StockMovementRepository movementRepository = new StockMovementRepository();

        InventoryService inventoryService = new InventoryService(productRepository, movementRepository);
        InventoryController inventoryController = new InventoryController(inventoryService);

        SalesService salesService = new SalesService(orderRepository, customerRepository, productRepository, inventoryService);

        ProductService productService = new ProductService(productRepository, salesService, inventoryService);
        ProductController productController = new ProductController(productService);

        CustomerService customerService = new CustomerService(customerRepository, orderRepository);
        CustomerController customerController = new CustomerController(customerService);

        ReturnRepository returnRepository = new ReturnRepository();
        ReturnService returnService = new ReturnService(salesService, returnRepository, inventoryService);
        SalesController salesController = new SalesController(salesService, customerService, productService, returnService);
        ReportService reportService = new ReportService(salesService, returnRepository);
        ReportController reportController = new ReportController(reportService);
        new MainController(productController, customerController, inventoryController, salesController, reportController).run();
    }
}