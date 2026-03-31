package com.busyrestaurant;

import com.busyrestaurant.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KitchenManager {
    private static KitchenManager instance;

    // ObservableList ensures the Kitchen UI refreshes automatically when orders arrive
    private final ObservableList<Order> activeOrders;

    private KitchenManager() {
        activeOrders = FXCollections.observableArrayList();
    }

    public static KitchenManager getInstance() {
        if (instance == null) {
            instance = new KitchenManager();
        }
        return instance;
    }

    public ObservableList<Order> getActiveOrders() {
        return activeOrders;
    }

    // Called when a customer clicks "PLACE ORDER"
    public void addOrder(Order order) {
        activeOrders.add(order);
    }

    // Called when the Chef clicks "Complete" or "Ready for Pickup"
    public void removeOrder(Order order) {
        activeOrders.remove(order);
    }

    /**
     * NEW: Clears all active orders from the kitchen.
     * Useful for the "Reset Summary" button to clear the board after a rush.
     */
    public void resetSummary() {
        activeOrders.clear();
    }
}