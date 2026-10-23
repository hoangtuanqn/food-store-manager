
package controller;

import exception.BusinessException;
import service.InventoryService;
import helper.InputHelper;
import view.ProductView;
public class InventoryController {
    private final InventoryService inventoryService;
 
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void run() {
        int choice;
        do {
            ProductView.showInventoryMenu();
            choice = InputHelper.readIntRange("Enter your choice: ", 0, 6);
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
            // [KIỂM TRA] đổi tên hàm cho khớp InputHelper của nhóm (readString/readInt/readIntRange)
            String id = InputHelper.readId("Enter product ID: ");
            int newQty = InputHelper.readIntRange("Enter new stock quantity: ",0);
            inventoryService.updateInventory(id, newQty);
            ProductView.showMessage("Stock updated successfully."); // [KIỂM TRA] tên hàm trong ProductView
        } catch (BusinessException e) {
            ProductView.showError(e.getMessage());
        }
    }
    public void viewMovements() {
        try {
            ProductView.showStockMovements(inventoryService.getStockMovements()); // [THÊM] hàm mới trong ProductView
        } catch (BusinessException e) {
            ProductView.showError(e.getMessage());
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
