/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author ad
 */
public class IdUtil {

    public static String normalize(String id) {
        return id == null ? null : id.trim().toUpperCase();
    }
}
