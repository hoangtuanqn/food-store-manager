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
    
    public static void showConfirmAction(String action) {
        System.out.println("[1] " + action + "\t[2] Cancel");
    }

    
}
