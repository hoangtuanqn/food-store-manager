/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import helper.FileHelper;
import helper.IdHelper;
import model.Customer;
import model.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 *
 * @author MSI
 */
public class FileOrderRepository implements OrderRepository {

    private final List<Order> orders;

    public FileOrderRepository() {
        this.orders = FileHelper.load(FileHelper.ORDERS, new ArrayList<Order>());
    }

    @Override
    public void linkCustomers(Repository<Customer, String> customerRepository) {
        orders.forEach(order -> customerRepository.findById(order.getCustomerId()).ifPresent(order::setCustomer));
    }

    private void save() {
        FileHelper.save(FileHelper.ORDERS, orders);
    }

    @Override
    public void add(Order item) {
        orders.add(item);
        save();
    }

    @Override
    public Optional<Order> findById(String id) {
        int index = indexOf(id);
        return index < 0 ? Optional.<Order>empty() : Optional.of(orders.get(index));
    }

    @Override
    public boolean existsById(String id) {
        return indexOf(id) >= 0;
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders);
    }

    @Override
    public void update(Order item) {
        int index = indexOf(item.getOrderId());
        if (index >= 0) {
            orders.set(index, item);
        } else {
            orders.add(item);
        }
        save();
    }

    @Override
    public boolean deleteById(String id) {
        int index = indexOf(id);
        if (index < 0) {
            return false;
        }
        orders.remove(index);
        save();
        return true;
    }

    @Override
    public int countByCustomerId(String customerId) {
        String normalizedId = IdHelper.normalize(customerId);
        return (int) orders.stream()
                .filter(order -> order.getCustomerId().equals(normalizedId))
                .count();
    }

    @Override
    public boolean existsByProductId(String productId) {
        return orders.stream().anyMatch(order -> order.findDetail(productId) != null);
    }

    @Override
    public boolean existsPendingByProductId(String productId) {
        return orders.stream()
                .anyMatch(order -> order.isPending() && order.findDetail(productId) != null);
    }

    private int indexOf(String id) {
        return IntStream.range(0, orders.size())
                .filter(index -> orders.get(index).getOrderId().equals(id))
                .findFirst()
                .orElse(-1);
    }
}
