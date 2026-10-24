/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author ad
 */
public class FileHelper {

    public static final String PRODUCTS = "data/products.dat";
    public static final String CUSTOMERS = "data/customers.dat";
    public static final String ORDERS = "data/orders.dat";
    public static final String RETURNS = "data/returns.dat";
    public static final String STOCK_MOVEMENTS = "data/stock_movements.dat";

    public static void save(String fileName, Object data) {
        File file = new File(fileName);
        file.getParentFile().mkdirs();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(data);
        } catch (IOException e) {
            System.err.println("Cannot save " + fileName + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(String fileName, T emptyData) {
        File file = new File(fileName);
        if (!file.exists()) {
            return emptyData;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (T) in.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            System.err.println("Cannot read " + fileName + ", starting with empty data.");
            return emptyData;
        }
    }
}
