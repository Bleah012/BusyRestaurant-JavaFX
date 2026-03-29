package com.busyrestaurant;

import com.busyrestaurant.model.MenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CartManager {
    private static CartManager instance;
    // ObservableList automatically notifies the UI when items are added
    private ObservableList<MenuItem> cartItems = FXCollections.observableArrayList();

    private CartManager() {}

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addItem(MenuItem item) {
        cartItems.add(item);
    }

    public ObservableList<MenuItem> getCartItems() {
        return cartItems;
    }

    public double calculateTotal() {
        return cartItems.stream().mapToDouble(MenuItem::getPrice).sum();
    }

    public void clearCart() {
        cartItems.clear();
    }
}