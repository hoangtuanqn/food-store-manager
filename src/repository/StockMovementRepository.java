/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;
import helper.FileHelper; 
import model.StockMovement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ad
 */
public class StockMovementRepository {
  private final List<StockMovement> movements;
       public StockMovementRepository() {
        this.movements = FileHelper.load(FileHelper.STOCK_MOVEMENTS, new ArrayList<StockMovement>());
    }
        private void save() {
        FileHelper.save(FileHelper.STOCK_MOVEMENTS, movements);
    }
         public void add(StockMovement movement) {
        movements.add(movement);
        save();       
    }
          public List<StockMovement> findAll() {
        return new ArrayList<>(movements);
    }

}
