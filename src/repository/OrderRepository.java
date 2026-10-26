/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import model.Customer;
import model.Order;

public interface OrderRepository extends Repository<Order, String> {

    void linkCustomers(Repository<Customer, String> customerRepository);

    int countByCustomerId(String customerId);

    boolean existsByProductId(String productId);

    boolean existsPendingByProductId(String productId);
}
