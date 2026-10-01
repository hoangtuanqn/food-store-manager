/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.repository;

import java.util.List;

/**
 *
 * @author MSI
 */
public interface Repository<T>{
    boolean add(T item);
    T findById(String id);
    List<T> findAll();
    boolean update(T item);
    boolean delete(String id);
    boolean existsById(String id);
}
