/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ad
 */
public class ProductSales {

    private final String productId;
    private final String productName;
    private final int quantitySold;

    public ProductSales(String productId, String productName, int quantitySold) {
        this.productId = productId;
        this.productName = productName;
        this.quantitySold = quantitySold;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }
}
