/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author MSI
 */
public interface Repository<T, ID>{
    void add(T item);
    Optional<T> findById(ID id);
    boolean existsById(ID id);
    List<T> findAll();
    void update(T item);
    boolean deleteById(ID id);
}
