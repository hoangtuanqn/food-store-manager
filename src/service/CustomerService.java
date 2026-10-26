/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import exception.DuplicateIdException;
import exception.DuplicatePhoneException;
import exception.EntityInUseException;
import helper.PhoneHelper;
import model.Customer;
import model.MembershipType;
import repository.CustomerRepository;
import repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author MSI
 */
public class CustomerService {

    private final CustomerRepository repository;
    private final OrderRepository orderRepository;

    public CustomerService(CustomerRepository repository, OrderRepository orderRepository) {
        this.repository = repository;
        this.orderRepository = orderRepository;
    }

    public String validatePhone(String phone, String ownerId) {
        String normalized = PhoneHelper.normalize(phone);
        repository.findAll().stream()
                .filter(customer -> customer.getPhone().equals(normalized) && !customer.getCustomerId().equals(ownerId))
                .findFirst()
                .ifPresent(owner -> {
                    throw new DuplicatePhoneException("Phone number already belongs to customer " + owner.getCustomerId() + ".");
                });
        return normalized;
    }

    public void addCustomer(Customer customer) {
        if (repository.existsById(customer.getCustomerId())) {
            throw new DuplicateIdException(
                    "Failed to add customer. Customer ID already exists."
            );
        }

        customer.setPhone(validatePhone(customer.getPhone(), customer.getCustomerId()));

        repository.add(customer);
    }

    public boolean existsById(String id) {
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
        Customer existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer ID does not exist."));

        String validPhone = validatePhone(phone, id);

        repository.update(Customer.create(existing.getCustomerId(), name, validPhone, address, type));
        orderRepository.linkCustomers(repository);
    }

    public void deleteCustomer(String id) {
        int transactionCount = orderRepository.countByCustomerId(id);
        if (transactionCount > 0) {
            throw new EntityInUseException(
                    "Customer " + id + " has " + transactionCount + " transactions and cannot be deleted."
            );
        }

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
