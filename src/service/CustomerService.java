package service;

import exception.DuplicateIdException;
import helper.IdHelper;
import model.Customer;
import model.MembershipType;
import repository.CustomerRepository;
import repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerService {

    private final CustomerRepository repository;
    private final OrderRepository orderRepository;

    public CustomerService(CustomerRepository repository, OrderRepository orderRepository) {
        this.repository = repository;
        this.orderRepository = orderRepository;
    }

    /**
     * Kiểm tra định dạng số điện thoại Việt Nam (10 chữ số bắt đầu bằng 0, hoặc dạng +84)
     * @param phone
     * @return 
     */
    public boolean isValidVietnamesePhone(String phone) {
        if (phone == null) return false;
        String phoneRegex = "^(0[3|5|7|8|9][0-9]{8}|\\+84[3|5|7|8|9][0-9]{8})$";
        return phone.matches(phoneRegex);
    }

    /**
     * Tìm khách hàng theo số điện thoại
     * @param phone
     * @return 
     */
    public Customer findCustomerByPhone(String phone) {
        if (phone == null) return null;
        return repository.findAll().stream()
                .filter(c -> phone.equals(c.getPhone()))
                .findFirst()
                .orElse(null);
    }

    public void addCustomer(Customer customer) {
        if (customer == null || customer.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer or Customer ID cannot be null.");
        }

        if (repository.existsById(customer.getCustomerId())) {
            throw new DuplicateIdException(
                    "Failed to add customer. Customer ID already exists."
            );
        }

        // Kiểm tra định dạng số điện thoại
        if (!isValidVietnamesePhone(customer.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number format. Must be 10 digits starting with 0 or +84.");
        }

        // Kiểm tra trùng số điện thoại với khách hàng khác
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
        String normalizedId = IdHelper.normalize(id);
        Customer c = repository.findById(normalizedId)
                .orElseThrow(() -> new IllegalArgumentException("Customer ID does not exist."));

        // Kiểm tra định dạng số điện thoại mới
        if (!isValidVietnamesePhone(phone)) {
            throw new IllegalArgumentException("Invalid phone number format. Must be 10 digits starting with 0 or +84.");
        }

        // Kiểm tra trùng số điện thoại nhưng phải KHÁC chính khách hàng đang được update
        Customer existingCustomer = findCustomerByPhone(phone);
        if (existingCustomer != null && !IdHelper.normalize(existingCustomer.getCustomerId()).equals(normalizedId)) {
            throw new IllegalStateException(
                    String.format("Phone number already belongs to customer %s.", existingCustomer.getCustomerId())
            );
        }

        c.setFullName(name);
        c.setPhone(phone);
        c.setAddress(address);
        c.setMembershipType(type);
        
        repository.update(c);
    }

    public void deleteCustomer(String id) {
        String normalizedId = IdHelper.normalize(id);
        // Kiểm tra xem khách hàng có đơn hàng nào không
        int transactionCount = orderRepository.countByCustomerId(normalizedId);
        if (transactionCount > 0) {
            throw new IllegalStateException(
                    String.format("Customer %s has %d transactions and cannot be deleted.", normalizedId, transactionCount)
            );
        }

        if (!repository.deleteById(normalizedId)) {
            throw new IllegalArgumentException(
                    "Customer ID does not exist."
            );
        }
    }

    public List<Customer> search(String keyword) {
        if (keyword == null) return new ArrayList<>();
        String searchKeyword = keyword.trim().toLowerCase();
        String normalizedKeyword = IdHelper.normalize(keyword).toLowerCase();
        
        return repository.findAll()
                .stream()
                .filter(customer ->
                        (customer.getCustomerId() != null && IdHelper.normalize(customer.getCustomerId()).toLowerCase().contains(normalizedKeyword))
                        || (customer.getFullName() != null && customer.getFullName().toLowerCase().contains(searchKeyword))
                        || (customer.getPhone() != null && customer.getPhone().contains(searchKeyword))
                )
                .collect(Collectors.toList());
    }
}