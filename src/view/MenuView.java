/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author MSI
 */
public class MenuView {

    private static final int WIDTH = 45;

    public static void showMainMenu() {
        printLine('=');
        printTitleCenter("FOOD SALES MANAGEMENT SYSTEM");
        printLine('=');
        String[] options = {
            "Manage Food Products",
            "Manage Customers",
            "Sales Management",
            "Inventory Management",
            "Reports"};
        IntStream.range(0, options.length)
                .forEach(index -> System.out.println((index + 1) + ". " + options[index]));
        System.out.println("0. Exit");
        printLine('-');
    }

    public static void showSubMenu(String title, String[] options) {
        printLine('=');
        printTitleCenter(title);
        printLine('=');
        IntStream.range(0, options.length)
                .forEach(index -> System.out.println((index + 1) + ". " + options[index]));
        System.out.println("0. Back");
        printLine('-');
    }

    public static void showTitle(String title) {
        System.out.println("----------- " + title + " -----------");
    }

    public static void showLine() {
        showLine(WIDTH);
    }

    public static void showLine(int width) {
        System.out.println(repeat('-', width));
    }

    private static void printLine(char character) {
        System.out.println(repeat(character, WIDTH));
    }

    private static void printTitleCenter(String title) {
        int padding = Math.max(0, (WIDTH - title.length()) / 2);
        System.out.println(repeat(' ', padding) + title);
    }

    private static String repeat(char character, int times) {
        return IntStream.range(0, times)
                .mapToObj(index -> String.valueOf(character))
                .collect(Collectors.joining());
    }
}
