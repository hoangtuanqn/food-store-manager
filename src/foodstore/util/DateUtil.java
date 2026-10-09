    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 *
 * @author ad
 */
public class DateUtil {
    public static final String PATTERN = "dd/MM/yyyy";

    public static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    
    public static LocalDate parse(String text) {
        return LocalDate.parse(text.trim(), FORMATTER);
    }
 
    public static String format(LocalDate date) {
        return date == null ? "" : date.format(FORMATTER);
    }

}
