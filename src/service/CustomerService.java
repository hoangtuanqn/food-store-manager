/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import exception.DuplicateIdException;
import exception.EntityInUseException;
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

    /**
     * Kiểm tra định dạng số điện thoại Việt Nam (10 chữ số bắt đầu bằng 0, hoặc dạng +84)
     */
    public boolean isValidVietnamesePhone(String phone) {
        if (phone == null) return false;
        String phoneRegex = "^(0[3|5|7|8|9][0-9]{8}|\\+84[3|5|7|8|9][0-9]{8})$";
        return phone.matches(phoneRegex);
    }

    /**
     * Tìm khách hàng theo số điện thoại
     */
    public Customer findCustomerByPhone(String phone) {
        if (phone == null) return null;
        return repository.findAll().stream()
                .filter(c -> phone.equals(c.getPhone()))
                .findFirst()
                .orElse(null);
    }

    public void addCustomer(Customer customer) {
        if (repository.existsById(customer.getCustomerId())) {
            throw new DuplicateIdException(
                    "Failed to add customer. Customer ID already exists."
            );
        }

        if (!isValidVietnamesePhone(customer.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number format. Must be 10 digits starting with 0 or +84.");
        }

        Customer existingCustomer = findCustomerByPhone(customer.getPhone());
        if (existingCustomer != null) {
            throw new IllegalStateException(
                    String.format("Phone number already belongs to customer %s.", existingCustomer.getCustomerId())
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

        if (!isValidVietnamesePhone(phone)) {
            throw new IllegalArgumentException("Invalid phone number format. Must be 10 digits starting with 0 or +84.");
        }

        Customer existingCustomer = findCustomerByPhone(phone);
        if (existingCustomer != null && !existingCustomer.getCustomerId().equals(id)) {
            throw new IllegalStateException(
                    String.format("Phone number already belongs to customer %s.", existingCustomer.getCustomerId())
            );
        }

        c.setFullName(name);
        c.setPhone(phone);
        c.setAddress(address);
        c.setMembershipType(type);
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