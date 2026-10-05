/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.service;
import foodstore.exception.*;
import foodstore.model.FoodProduct;
import foodstore.repository.FoodProductRepository;
import java.util.List;
import java.util.stream.Collectors; 
public class InventoryService {
    public static final int LOW_STOCK_THRESHOLD = 5;
    public static final int NEAR_EXPIRATION_DAYS = 7;
    private final FoodProductRepository productRepository;
    public InventoryService(FoodProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public void checkSellable (String productId , int quantity) {    
        FoodProduct p = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found: " + productId));
        if(quantity <= 0){
            throw new InvalidQuantityException("Quantity must be greater than 0 " );
        }
        if(p.isExpired()){
            throw new ExpiredProductException("The product was expired " +p.getName());
        }
        if(p.getQuantity()< quantity){
            throw new InsufficientStockException("The product was out of stock. " +p.getQuantity()+ " is available");
        }
}
   public void deductInventory(String productId, int quantity) {
    FoodProduct p = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found: " + productId));
    p.setQuantity(p.getQuantity() - quantity);  
    productRepository.update(p);
}
   public void updateInventory (String productId, int newQuantity) {
        if (newQuantity < 0) {
            throw new InvalidQuantityException("Stock quantity cannot be negative");
        }
        FoodProduct p = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: " + productId));
        p.setQuantity(newQuantity);
        productRepository.update(p);
    }
   public List<FoodProduct> getLowStockProducts(){
         return productRepository.findAll().stream()
                .filter(FoodProduct::isLowStock)
                .collect(Collectors.toList());
   }
   public List<FoodProduct> getExpiredProducts(){
       return productRepository.findAll().stream()
               .filter (FoodProduct::isExpired)
               .collect(Collectors.toList());
               
   }
   public List<FoodProduct> getNearExpiryProducts(){
       return productRepository.findAll().stream()
               .filter( FoodProduct:: isNearExpiration)
               .collect(Collectors.toList());
       
   }
   public List<FoodProduct> getSellableProducts(){
       return productRepository.findAll().stream()
               .filter(p -> !p.isExpired() && p.getQuantity() > 0)
                .collect(Collectors.toList());

   }
}
 

