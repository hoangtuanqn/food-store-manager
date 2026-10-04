/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.repository;
import foodstore.model.Customer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author MSI
 */
public class CustomerRepository implements Repository<Customer, String> {

    private final Map<String, Customer> customers;

    public CustomerRepository() {
        this.customers = new LinkedHashMap<>();
    }

    @Override
    public void add(Customer item) {
        customers.put(item.getCustomerId(), item);
    }

    @Override
    public Optional<Customer> findById(String id) {
        return Optional.ofNullable(customers.get(id));
    }

    @Override
    public boolean existsById(String id) {
        return customers.containsKey(id);
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customers.values());
    }

    @Override
    public void update(Customer item) {
        customers.put(item.getCustomerId(), item);
    }

    @Override
    public boolean deleteById(String id) {
        return customers.remove(id) != null;
    }
}
