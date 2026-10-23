/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import helper.IdHelper;
import model.Customer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 *
 * @author MSI
 */
public class CustomerRepository implements Repository<Customer, String> {

    private final List<Customer> customers;

    public CustomerRepository() {
        this.customers = new ArrayList<>();
//      seedData();
    }

    @Override
    public void add(Customer item) {
        customers.add(item);
    }

    @Override
    public Optional<Customer> findById(String id) {
        int index = indexOf(id);
        return index < 0 ? Optional.empty() : Optional.of(customers.get(index));
    }

    @Override
    public boolean existsById(String id) {
        return indexOf(id) >= 0;
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customers);
    }

    @Override
    public void update(Customer item) {
        int index = indexOf(item.getCustomerId());
        if (index >= 0) {
            customers.set(index, item);
        } else {
            customers.add(item);
        }
    }

    @Override
    public boolean deleteById(String id) {
        int index = indexOf(id);
        if (index < 0) {
            return false;
        }
        customers.remove(index);
        return true;
    }

    private int indexOf(String id) {
        if (id == null) return -1;
        String normalizedSearchId = IdHelper.normalize(id);
        return IntStream.range(0, customers.size())
                .filter(i -> {
                    String currentId = customers.get(i).getCustomerId();
                    return currentId != null && IdHelper.normalize(currentId).equals(normalizedSearchId);
                })
                .findFirst()
                .orElse(-1);
    }
}