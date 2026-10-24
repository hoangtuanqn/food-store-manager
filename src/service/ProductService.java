/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import exception.BusinessException;
import exception.DuplicateIdException;
import exception.NotFoundException;
import model.FoodProduct;
import repository.FoodProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Optional;
import java.time.LocalDate;

/**
 *
 * @author MSI
 */
public class ProductService {

    private final FoodProductRepository repository;
    private final SalesService salesService;
    private final InventoryService inventoryService;

    public ProductService(FoodProductRepository repository, SalesService salesService, InventoryService inventoryService) {
        this.repository = repository;
        this.salesService = salesService;
        this.inventoryService = inventoryService;
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
        FoodProduct current = repository.findById(product.getProductId())
                .orElseThrow(() -> new NotFoundException("Product ID is not found. Failed to update."));
        int oldQuantity = current.getQuantity();

        repository.update(product);
        inventoryService.recordManualUpdate(product.getProductId(), oldQuantity, product.getQuantity());
    }

    public List<FoodProduct> searchKeyWords(String keyWords) {
        if (keyWords == null || keyWords.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String lowerKeyWords = keyWords.trim().toLowerCase();

        return repository.findAll().stream()
                .filter(product -> containsIgnoreCase(product.getName(), lowerKeyWords)
                || containsIgnoreCase(product.getCategory(), lowerKeyWords))
                .collect(Collectors.toList());
    }

    public List<FoodProduct> searchExpirationDate(LocalDate date) {
        if (date == null) {
            return new ArrayList<>();
        }
        return repository.findAll().stream()
                .filter(product -> date.equals(product.getExpirationDate()))
                .collect(Collectors.toList());
    }

    private static boolean containsIgnoreCase(String text, String lowerKeyWords) {
        return text != null && text.toLowerCase().contains(lowerKeyWords);
    }

    public void deleteProduct(String id) {
        Optional<FoodProduct> find = repository.findById(id);
        if(find.isPresent() == false){
            throw new NotFoundException("Product ID is not found.");
        }

        FoodProduct product = find.get();
        if(salesService.isProductUsedInAnyOrder(product.getProductId()) ){
            throw new BusinessException("Product " + product.getProductId() + 
                    "cannot be deleted" );
        }
        repository.deleteById(product.getProductId());
    }
}
