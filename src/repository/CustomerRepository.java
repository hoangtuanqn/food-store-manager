/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import helper.FileHelper;
import helper.IdHelper;
import model.Customer;
import model.MembershipType;
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
        this.customers = FileHelper.load(FileHelper.CUSTOMERS, new ArrayList<Customer>());
//        seedData();
    }

//    private void seedData() {
//        customers.add(new Customer("C001", "Pham Hoang Tuan", "0812665001", "Quang Ngai, Viet Nam", MembershipType.REGULAR));
//    }

    private void save() {
        FileHelper.save(FileHelper.CUSTOMERS, customers);
    }

    @Override
    public void add(Customer item) {
        customers.add(item);
        save();
    }

    @Override
    public Optional<Customer> findById(String id) {
        int index = indexOf(id);
        return index < 0 ? Optional.<Customer>empty() : Optional.of(customers.get(index));
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
        save();
    }

    @Override
    public boolean deleteById(String id) {
        int index = indexOf(id);
        if (index < 0) {
            return false;
        }
        customers.remove(index);
        save();
        return true;
    }

    private int indexOf(String id) {
        String normalizedId = IdHelper.normalize(id);
        return IntStream.range(0, customers.size())
                .filter(index -> customers.get(index).getCustomerId().equals(normalizedId))
                .findFirst()
                .orElse(-1);
    }
}
