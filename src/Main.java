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
        // 1. Khởi tạo các Repository dùng chung
        FoodProductRepository productRepository = new FoodProductRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        OrderRepository orderRepository = new OrderRepository();

        // 2. Khởi tạo Inventory
        InventoryService inventoryService = new InventoryService(productRepository);
        InventoryController inventoryController = new InventoryController(inventoryService);

        // 3. Khởi tạo Sales Service
        SalesService salesService = new SalesService(orderRepository, customerRepository, productRepository, inventoryService);

        // 4. Khởi tạo Product Service & Controller
        ProductService productService = new ProductService(productRepository, salesService);   
        ProductController productController = new ProductController(productService);

        // 5. Khởi tạo Customer Service với ĐỦ 2 THAM SỐ (để hỗ trợ chặn xóa theo yêu cầu của bạn)
        CustomerService customerService = new CustomerService(customerRepository, orderRepository);
        CustomerController customerController = new CustomerController(customerService);

        // 6. Khởi tạo Sales Controller (Đủ 3 tham số: SalesService, CustomerService, ProductService)
        SalesController salesController = new SalesController(salesService, customerService, productService);

        // 7. Khởi tạo MainController và gọi .run() để hiển thị Menu chính lên màn hình
        MainController mainController = new MainController(productController, customerController, inventoryController, salesController);
        mainController.run();
    }
}