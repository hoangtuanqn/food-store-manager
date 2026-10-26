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
public class FileReturnRepository implements ReturnRepository {

    private final List<ReturnOrder> returns = FileHelper.load(FileHelper.RETURNS, new ArrayList<ReturnOrder>());

    @Override
    public String nextId() {
        return String.format("R%02d", returns.size() + 1);
    }

    @Override
    public void add(ReturnOrder returnOrder) {
        returns.add(returnOrder);
        FileHelper.save(FileHelper.RETURNS, returns);
    }

    @Override
    public List<ReturnOrder> findAll() {
        return new ArrayList<>(returns);
    }

    @Override
    public List<ReturnOrder> findByOrderId(String orderId) {
        return returns.stream()
                .filter(returnOrder -> returnOrder.getOrderId().equals(orderId))
                .collect(Collectors.toList());
    }

    @Override
    public int countReturned(String orderId, String productId) {
        return findByOrderId(orderId).stream()
                .flatMap(returnOrder -> returnOrder.getDetails().stream())
                .filter(detail -> detail.getProductId().equals(productId))
                .mapToInt(detail -> detail.getQuantity())
                .sum();
    }
}
