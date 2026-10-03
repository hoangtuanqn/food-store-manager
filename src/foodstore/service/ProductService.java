/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.service;

import foodstore.execption.DuplicateIdException;
import foodstore.model.FoodProduct;
import foodstore.repository.FoodProductRepository;

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
        if(repository.existsById(product.getProductId())) {
            throw new DuplicateIdException("Failed to add product. Product ID already exists.");
        }
        repository.add(product);
    }
    
    public boolean checkIdExist(String id) {
        return repository.existsById(id);
    }
    
}
