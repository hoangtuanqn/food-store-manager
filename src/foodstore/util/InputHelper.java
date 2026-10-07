/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foodstore.util;

import foodstore.view.ConsoleView;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 *
 * @author ad
 */
public class InputHelper {

    private static final Scanner sc = new Scanner(System.in);

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number. Please try again.");
            }
        }
    }

    public static Double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number. Please try again.");
            }
        }
    }

    public static int readIntRange(String prompt, int min) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value < min) {
                    System.err.println("The number must be greater than or equal to " + min + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.err.println("Invalid number. Please try again.");
            }
        }
    }

    public static int readIntRange(String prompt, int min, int max) {
        while (true) {
            int value = InputHelper.readIntRange(prompt, min);
            if (value > max) {
                System.err.println("The number must be less than or equal to " + max + ".");
                continue;
            }
            return value;
        }
    }

    public static Double readPrice(String prompt) {
        while (true) {
            Double price = readDouble(prompt);
            if (price < 0) {
                System.err.println("The amount cannot be negative.");
                continue;
            }
            return price;
        }
    }

    public static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                System.err.println("Input must not be empty. Please try again.");
                continue;
            }
            return line;
        }
    }

    public static String readStringOrDefault(String prompt, String currentValue) {
        System.out.print(prompt + "[" + currentValue + "]: ");
        String line = sc.nextLine().trim();
        return line.isEmpty() ? currentValue : line;
    }

    public static int readIntRangeOrDefault(String prompt, int min, int max, int currentValue) {
        while (true) {
            System.out.print(prompt + "[" + currentValue + "]: ");
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                return currentValue;
            }
            try {
                int value = Integer.parseInt(line);
                if (value < min || value > max) {
                    System.err.println("The number must be between " + min + " and " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.err.println("Invalid number. Please try again.");
            }
        }
    }

    public static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return DateUtil.parse(line);
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date. Please use " + DateUtil.PATTERN + " (e.g. 01/12/2025).");
            }
        }
    }
    
    public static boolean confirmAction(String action) {
        ConsoleView.showConfirmAction(action);
        return InputHelper.readIntRange("Choice: ", 1, 2) == 1;
    }
}
