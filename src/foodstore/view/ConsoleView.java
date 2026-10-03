/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.view;

/**
 *
 * @author ad
 */
public class ConsoleView {
    public static void showError(String message) {
        System.err.println(message);
    }
    
    public static void showSuccess(String message) {
        System.out.println("[SUCCESS] " + message);
    }
}
