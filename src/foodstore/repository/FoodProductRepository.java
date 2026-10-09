/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.repository;

import foodstore.model.FoodProduct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author MSI
 */
public class FoodProductRepository implements Repository<FoodProduct, String> {

    private final List<FoodProduct> products;

    public FoodProductRepository() {
        this.products = new ArrayList<>();
//        seedData();
    }

//    private void seedData() {
//        products.add(new FoodProduct("P001", "Banh mi", "Tinh bot", "Cai", 3000, 100, LocalDate.now(), LocalDate.parse("2027-01-10")));
//    }

    @Override
    public void add(FoodProduct item) {
        products.add(item);
    }

    @Override
    public Optional<FoodProduct> findById(String id) {
        int index = indexOf(id);
        return index < 0 ? Optional.<FoodProduct>empty() : Optional.of(products.get(index));
    }

    @Override
    public boolean existsById(String id) {
        return indexOf(id) >= 0;
    }

    @Override
    public List<FoodProduct> findAll() {
        return new ArrayList<>(products);
    }

    @Override
    public void update(FoodProduct item) {
        int index = indexOf(item.getProductId());
        if (index >= 0) {
            products.set(index, item);
        } else {
            products.add(item);
        }
    }

    @Override
    public boolean deleteById(String id) {
        int index = indexOf(id);
        if (index < 0) {
            return false;
        }
        products.remove(index);
        return true;
    }

    private int indexOf(String id) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductId().equals(id)) {
                return i;
            }
        }
        return -1;
    }
}
