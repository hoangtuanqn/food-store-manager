/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import helper.FileHelper;
import model.ReturnOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author ad
 */
public class ReturnRepository {

    private final List<ReturnOrder> returns = FileHelper.load(FileHelper.RETURNS, new ArrayList<ReturnOrder>());

    public String nextId() {
        return String.format("R%02d", returns.size() + 1);
    }

    public void add(ReturnOrder returnOrder) {
        returns.add(returnOrder);
        FileHelper.save(FileHelper.RETURNS, returns);
    }

    public List<ReturnOrder> findAll() {
        return new ArrayList<>(returns);
    }

    public List<ReturnOrder> findByOrderId(String orderId) {
        return returns.stream()
                .filter(r -> r.getOrderId().equals(orderId))
                .collect(Collectors.toList());
    }

    public int countReturned(String orderId, String productId) {
        return findByOrderId(orderId).stream()
                .flatMap(r -> r.getDetails().stream())
                .filter(d -> d.getProductId().equals(productId))
                .mapToInt(d -> d.getQuantity())
                .sum();
    }
}
