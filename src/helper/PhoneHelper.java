/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

import java.util.regex.Pattern;

/**
 *
 * @author ad
 */
public class PhoneHelper {

    private static final Pattern VN_PHONE = Pattern.compile("^(0|\\+84)\\d{9}$");

    public static String normalize(String phone) {
        String trimmed = phone == null ? "" : phone.trim();
        if (!VN_PHONE.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("Invalid phone number. Use 10 digits starting with 0, or +84 followed by 9 digits.");
        }
        return trimmed.startsWith("+84") ? "0" + trimmed.substring(3) : trimmed;
    }
}
