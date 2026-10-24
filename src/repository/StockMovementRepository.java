/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import model.StockMovement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ad
 */
public class StockMovementRepository {

    private final List<StockMovement> movements = new ArrayList<>();

    public void add(StockMovement movement) {
        movements.add(movement);
    }

    public List<StockMovement> findAll() {
        return new ArrayList<>(movements);
    }
}
