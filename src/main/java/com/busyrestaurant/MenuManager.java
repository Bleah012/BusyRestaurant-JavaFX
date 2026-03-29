package com.busyrestaurant;

import com.busyrestaurant.model.MenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MenuManager {
    private static MenuManager instance;

    // ObservableList allows the UI to "listen" for CRUD changes automatically
    private final ObservableList<MenuItem> allItems;

    private MenuManager() {
        allItems = FXCollections.observableArrayList();
        loadDefaultMenu();
    }

    public static MenuManager getInstance() {
        if (instance == null) {
            instance = new MenuManager();
        }
        return instance;
    }

    /**
     * Initial data for the system.
     * In a real app, this could load from a Database or JSON file.
     */
    private void loadDefaultMenu() {
        allItems.add(new MenuItem("1", "Classic Burger", "Beef patty", 12.50, "Main Courses", "burger.jpg"));
        allItems.add(new MenuItem("2", "Veggie Pizza", "Fresh veggies", 15.00, "Main Courses", "pizza.jpg"));
        allItems.add(new MenuItem("3", "Pasta Carbonara", "Bacon & Cream", 14.20, "Main Courses", "pasta.jpg"));
        allItems.add(new MenuItem("4", "Chicken Salad", "Grilled chicken", 10.00, "Appetizers", "salad.jpg"));
        allItems.add(new MenuItem("5", "Coca Cola", "Chilled soda", 2.50, "Drinks", "burger.jpg")); // Reuse burger if soda.jpg missing
    }

    // --- CRUD OPERATIONS ---

    // CREATE
    public void addItem(MenuItem item) {
        allItems.add(item);
    }

    // READ
    public ObservableList<MenuItem> getAllItems() {
        return allItems;
    }

    // UPDATE
    public void updateItem(int index, MenuItem newItem) {
        if (index >= 0 && index < allItems.size()) {
            allItems.set(index, newItem);
        }
    }

    // DELETE
    public void removeItem(MenuItem item) {
        allItems.remove(item);
    }
}