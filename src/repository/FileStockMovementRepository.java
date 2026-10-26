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
public class FileStockMovementRepository implements StockMovementRepository {

    private final List<StockMovement> movements = FileHelper.load(FileHelper.STOCK_MOVEMENTS, new ArrayList<StockMovement>());

    @Override
    public void add(StockMovement movement) {
        movements.add(movement);
        FileHelper.save(FileHelper.STOCK_MOVEMENTS, movements);
    }

    @Override
    public List<StockMovement> findAll() {
        return new ArrayList<>(movements);
    }
}
