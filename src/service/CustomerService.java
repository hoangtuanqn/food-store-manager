package service;

import exception.DuplicateIdException;
import model.Customer;
import model.MembershipType;
import repository.CustomerRepository;
import repository.OrderRepository; // 1. Nhớ import OrderRepository
import java.util.List;
import java.util.stream.Collectors;

public class CustomerService {

    private final CustomerRepository repository;
    private final OrderRepository orderRepository; // 2. Khai báo OrderRepository

    // 3. Cập nhật Constructor nhận ĐỦ 2 tham số
    public CustomerService(CustomerRepository repository, OrderRepository orderRepository) {
        this.repository = repository;
        this.orderRepository = orderRepository;
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
        // Kiểm tra xem khách hàng có đơn hàng nào không (chặn xóa nếu có >= 1 giao dịch)
        int transactionCount = orderRepository.countByCustomerId(id);
        if (transactionCount > 0) {
            throw new IllegalStateException(
                    String.format("Customer %s has %d transactions and cannot be deleted.", id, transactionCount)
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