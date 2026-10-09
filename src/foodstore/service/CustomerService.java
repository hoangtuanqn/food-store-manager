/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.service;
import foodstore.exception.DuplicateIdException;
import foodstore.model.Customer;
import foodstore.model.MembershipType;
import foodstore.repository.CustomerRepository;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 * @author MSI
 */
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public void addCustomer(Customer customer) {
        if (repository.existsById(customer.getCustomerId())) {
            throw new DuplicateIdException(
                    "Failed to add customer. Customer ID already exists."
            );
        }

        repository.add(customer);
    }

    public boolean checkIdExist(String id) {
        return repository.existsById(id);
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Customer findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public void updateCustomer(String id, String name, String phone,
                               String address, MembershipType type) {
        Customer c = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer ID does not exist."));
        c.setFullName(name);
        c.setPhone(phone);
        c.setAddress(address);
        c.setMembershipType(type);
    }

    public void deleteCustomer(String id) {
        if (!repository.deleteById(id)) {
            throw new IllegalArgumentException(
                    "Customer ID does not exist."
            );
        }
    }

    public List<Customer> search(String keyword) {
        String searchKeyword = keyword.trim().toLowerCase();

        return repository.findAll()
                .stream()
                .filter(customer ->
                        customer.getFullName().toLowerCase().contains(searchKeyword)
                        || customer.getPhone().contains(searchKeyword)
                )
                .collect(Collectors.toList());
    }
}
