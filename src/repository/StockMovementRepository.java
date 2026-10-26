/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import model.StockMovement;
import java.util.List;

public interface StockMovementRepository {

    void add(StockMovement movement);

    List<StockMovement> findAll();
}
