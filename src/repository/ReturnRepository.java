/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import model.ReturnOrder;
import java.util.List;

public interface ReturnRepository {

    String nextId();

    void add(ReturnOrder returnOrder);

    List<ReturnOrder> findAll();

    List<ReturnOrder> findByOrderId(String orderId);

    int countReturned(String orderId, String productId);
}
