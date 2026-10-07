
package foodstore.controller;

import foodstore.exception.BusinessException;
import foodstore.service.InventoryService;
import foodstore.util.InputHelper;
import foodstore.view.ProductView;
public class InventoryController {
    private final InventoryService inventoryService;
 
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void run() {
        int choice;
        do {
            ProductView.showInventoryMenu();
            choice = InputHelper.readIntRange("Enter your choice: ", 0, 4);
            if (choice != 0) {
                handle(choice);
            }
        } while (choice != 0);
    }
 
    public void handle(int choice) {
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
            default: ProductView.showError("Invalid choice.");
                break;
        }
    }
 
    public void viewLowStock() {
        try {
            ProductView.showLowStockProducts(inventoryService.getLowStockProducts());
        } catch (BusinessException e) {
            ProductView.showError(e.getMessage());
        }
    }
 
               
    public void viewExpired() {
        try {
            ProductView.showExpiredProducts(inventoryService.getExpiredProducts());
        } catch (BusinessException e) {
            ProductView.showError(e.getMessage());
        }
    }
 
    public void viewNearExpiry() {
        try {
            ProductView.showNearExpiryProducts(inventoryService.getNearExpiryProducts());
        } catch (BusinessException e) {
            ProductView.showError(e.getMessage());
        }
    }
 
    public void viewSellable() {
        try {
            ProductView.showSellableProducts(inventoryService.getSellableProducts());
        } catch (BusinessException e) {
            ProductView.showError(e.getMessage());
        }
    }
}
