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
        // We no longer call loadDefaultMenu() here because
        // loadFromDatabase() will be called from the Application start method.
    }

    public static MenuManager getInstance() {
        if (instance == null) {
            instance = new MenuManager();
        }
        return instance;
    }

    /**
     * READ: Loads all items from the H2 Database into the ObservableList.
     */
    public void loadFromDatabase() {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            List<MenuItem> fromDb = em.createQuery("SELECT m FROM MenuItem m", MenuItem.class).getResultList();
            allItems.setAll(fromDb);
            System.out.println("Successfully loaded " + fromDb.size() + " items from Database.");
        } catch (Exception e) {
            System.err.println("Error loading from database: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    // --- DATABASE CRUD OPERATIONS ---

    /**
     * CREATE: Saves a new item to the database and updates the UI list.
     */
    public void addItem(MenuItem item) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(item); // Saves to the .mv.db file
            em.getTransaction().commit();

            allItems.add(item); // Updates the JavaFX UI
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * READ: Returns the list for the TableView and MenuView.
     */
    public ObservableList<MenuItem> getAllItems() {
        return allItems;
    }

    /**
     * UPDATE: Updates an existing item in the database.
     */
    public void updateItem(MenuItem item) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(item); // Updates the existing record based on ID
            em.getTransaction().commit();

            // Refresh the local list to ensure UI is in sync
            loadFromDatabase();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * DELETE: Removes an item from the database and the UI list.
     */
    public void removeItem(MenuItem item) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            em.getTransaction().begin();
            // Find the "managed" version of the object before removing
            MenuItem managedItem = em.find(MenuItem.class, item.getId());
            if (managedItem != null) {
                em.remove(managedItem);
            }
            em.getTransaction().commit();

            allItems.remove(item); // Update UI
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}