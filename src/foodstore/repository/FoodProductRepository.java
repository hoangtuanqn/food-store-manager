/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.repository;
import foodstore.model.FoodProduct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author MSI
 */
public class FoodProductRepository implements Repository<FoodProduct, String>{

    private final Map<String, FoodProduct> products;
    public FoodProductRepository() {
        this.products = new LinkedHashMap<>();
    }
    
    
    @Override
    public void add(FoodProduct item) {
        this.products.put(item.getProductId(), item);
    }

    @Override
    public Optional<FoodProduct> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public boolean existsById(String id) {
        return products.containsKey(id);
    }

    @Override
    public List<FoodProduct> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(FoodProduct item) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean deleteById(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}