/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.repository;

import foodstore.model.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author MSI
 */
public class OrderRepository implements Repository<Order, String> {

    private final List<Order> orders;

    public OrderRepository() {
        this.orders = new ArrayList<>();
    }

    @Override
    public void add(Order item) {
        orders.add(item);
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
    }

    @Override
    public boolean deleteById(String id) {
        int index = indexOf(id);
        if (index < 0) {
            return false;
        }
        orders.remove(index);
        return true;
    }

    private int indexOf(String id) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderId().equals(id)) {
                return i;
            }
        }
        return -1;
    }
}
