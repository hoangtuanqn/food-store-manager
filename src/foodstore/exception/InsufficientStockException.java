/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.exception;

/**
 *
 * @author MSI
 */
public class InsufficientStockException extends BusinessException {
    public InsufficientStockException(String message) {
        super(message);
    }
}