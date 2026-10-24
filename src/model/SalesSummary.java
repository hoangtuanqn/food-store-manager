/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ad
 */
public class SalesSummary {

    private final int totalTransactions;
    private final int totalProductsSold;
    private final double totalRevenue;

    public SalesSummary(int totalTransactions, int totalProductsSold, double totalRevenue) {
        this.totalTransactions = totalTransactions;
        this.totalProductsSold = totalProductsSold;
        this.totalRevenue = totalRevenue;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public int getTotalProductsSold() {
        return totalProductsSold;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }
}
