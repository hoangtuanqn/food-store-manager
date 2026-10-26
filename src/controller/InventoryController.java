
package controller;

import exception.BusinessException;
import service.InventoryService;
import service.StockAlertService;
import helper.InputHelper;
import view.ProductView;
public class InventoryController {
    private final InventoryService inventoryService;
    private final StockAlertService stockAlertService;

    public InventoryController(InventoryService inventoryService, StockAlertService stockAlertService) {
        this.inventoryService = inventoryService;
        this.stockAlertService = stockAlertService;
    }

    public void run() {
        int choice;
        do {
            ProductView.showInventoryMenu();
            choice = InputHelper.readIntRange("Enter your choice: ", 0, 6);
            if (choice != 0) {
                handleChoice(choice);
            }
        } while (choice != 0);
    }

    public void handleChoice(int choice) {
        switch (choice) {
            case 1:
                viewLowStock();
                break;
            case 2:
                viewExpired();
                break;
            case 3:
                viewNearExpiry();
                break;
            case 4:
                viewSellable();
                break;
            case 5:
                updateStock();
                break;
            case 6:
                viewMovements();
                break;
            default: ProductView.showError("Invalid choice.");
                break;
        }
    }

    public void updateStock() {
        try {
            String id = InputHelper.readId("Enter product ID: ");
            int newQuantity = InputHelper.readIntRange("Enter new stock quantity: ", 0);
            inventoryService.updateInventory(id, newQuantity);
            ProductView.showMessage("Stock updated successfully.");
        } catch (BusinessException e) {
            ProductView.showError(e.getMessage());
        }
    }

    public void viewMovements() {
        ProductView.showStockMovements(inventoryService.getStockMovements());
    }

    public void viewLowStock() {
        try {
            ProductView.showLowStockProducts(stockAlertService.getLowStockProducts());
        } catch (BusinessException e) {
            ProductView.showError(e.getMessage());
        }
    }

    public void viewExpired() {
        try {
            ProductView.showExpiredProducts(stockAlertService.getExpiredProducts());
        } catch (BusinessException e) {
            ProductView.showError(e.getMessage());
        }
    }

    public void viewNearExpiry() {
        try {
            ProductView.showNearExpiryProducts(stockAlertService.getNearExpiryProducts());
        } catch (BusinessException e) {
            ProductView.showError(e.getMessage());
        }
    }

    public void viewSellable() {
        try {
            ProductView.showSellableProducts(stockAlertService.getSellableProducts());
        } catch (BusinessException e) {
            ProductView.showError(e.getMessage());
        }
    }
}
