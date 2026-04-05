package com.busyrestaurant;

import com.busyrestaurant.model.MenuItem;
import com.busyrestaurant.util.DatabaseManager;
import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class MenuManager {
    private static MenuManager instance;

    // ObservableList allows the UI to "listen" for CRUD changes automatically
    private final ObservableList<MenuItem> allItems;

    private MenuManager() {
        allItems = FXCollections.observableArrayList();
        // Initial load from DB
        loadFromDatabase();

        // SAFETY: If DB is empty, load defaults so the UI isn't blank during your demo
        if (allItems.isEmpty()) {
            loadDefaultMenu();
        }
    }

    public static MenuManager getInstance() {
        if (instance == null) {
            instance = new MenuManager();
        }
        return instance;
    }

    /**
     * READ: Loads all items from the H2 Database.
     */
    public void loadFromDatabase() {
        EntityManager em = null;
        try {
            em = DatabaseManager.getEntityManager();
            List<MenuItem> fromDb = em.createQuery("SELECT m FROM MenuItem m", MenuItem.class).getResultList();
            allItems.setAll(fromDb);
            System.out.println("Successfully loaded " + fromDb.size() + " items from Database.");
        } catch (Exception e) {
            System.err.println("Database Connection Error: " + e.getMessage());
            // If DB fails, we still want the app to run with defaults loaded in constructor
        } finally {
            if (em != null) em.close();
        }
    }

    /**
     * Safety Fallback: Populates menu if the database is fresh/empty.
     */
    private void loadDefaultMenu() {
        System.out.println("Database empty. Loading default demo menu...");
        // These now compile because of your MenuItem.java update
        addItem(new MenuItem("Classic Burger", "Juicy beef patty with cheese", 850.0, "Main Courses", "Extra Cheese, No Onions"));
        addItem(new MenuItem("Margherita Pizza", "Fresh basil and mozzarella", 1200.0, "Main Courses", "Thin Crust, Extra Sauce"));
        addItem(new MenuItem("Cappuccino", "Rich espresso with steamed milk", 350.0, "Drinks", "Sugar-free, Almond Milk"));
        addItem(new MenuItem("Chocolate Brownie", "Warm brownie with ice cream", 450.0, "Desserts", "Extra Scoop"));
    }

    public ObservableList<MenuItem> getAllItems() {
        return allItems;
    }

    // --- DATABASE CRUD OPERATIONS ---

    public void addItem(MenuItem item) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(item);
            em.getTransaction().commit();

            // Add to list if not already present (prevents UI duplicates)
            if (!allItems.contains(item)) {
                allItems.add(item);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void updateItem(MenuItem item) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(item);
            em.getTransaction().commit();
            loadFromDatabase(); // Refresh UI list from the source of truth
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void removeItem(MenuItem item) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            em.getTransaction().begin();
            MenuItem managedItem = em.find(MenuItem.class, item.getId());
            if (managedItem != null) {
                em.remove(managedItem);
            }
            em.getTransaction().commit();
            allItems.remove(item);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}