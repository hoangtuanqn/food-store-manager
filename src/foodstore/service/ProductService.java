/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.service;

import foodstore.exception.DuplicateIdException;
import foodstore.exception.NotFoundException;
import foodstore.model.FoodProduct;
import foodstore.repository.FoodProductRepository;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.time.LocalDate;

/**
 *
 * @author MSI
 */
public class ProductService {

    private final FoodProductRepository repository;

    public ProductService(FoodProductRepository repository) {
        this.repository = repository;
    }

    public void addProduct(FoodProduct product) {
        if (repository.existsById(product.getProductId())) {
            throw new DuplicateIdException("Failed to add product. Product ID already exists.");
        }
        repository.add(product);
    }

    public boolean checkIdExist(String id) {
        return repository.existsById(id);
    }
    
    public FoodProduct getProduct(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Not found Product ID " + id + "."));
    }

    public List<FoodProduct> getAllProducts() {
        return repository.findAll();
    }

    public FoodProduct getProductById(String id) {
        Optional<FoodProduct> product = repository.findById(id);
        if (product.isPresent()) {
            return product.get();
        }
        throw new NotFoundException("Product ID is not found");
    }

    public void updateProduct(FoodProduct product) {
        if (!repository.existsById(product.getProductId())) {
            throw new NotFoundException("Product ID is not found. Failed to update.");
        }
        
        repository.update(product);
    }

    public List<FoodProduct> searchKeyWords(String keyWords) {
        List<FoodProduct> result = new ArrayList<>();
        if (keyWords == null || keyWords.trim().isEmpty()) {
            return result;
        }

        String lowerKeyWords = keyWords.trim().toLowerCase();

        for (FoodProduct product : repository.findAll()) {
            String name = product.getName();
            String category = product.getCategory();
            if (name != null && name.toLowerCase().contains(lowerKeyWords)) {
                result.add(product);
            } else if (category != null && category.toLowerCase().contains(lowerKeyWords)) {
                result.add(product);
            }
        }
        return result;
    }

    public List<FoodProduct> searchExpirationDate(LocalDate date) {
        List<FoodProduct> result = new ArrayList<>();

        if (date == null) {
            return result;
        }
        for (FoodProduct object : repository.findAll()) {
            if (date.equals(object.getExpirationDate())) {
                result.add(object);
            }
        }
        return result;
    }
    

    public void deleteProduct(String id) {
        if (repository.deleteById(id) == false) {
            throw new NotFoundException("Product ID is not found. Failed to delete.");
        }
    }
}
