package com.busyrestaurant.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utility class to manage the JPA EntityManagerFactory.
 * This acts as the central hub for database connections.
 */
public class DatabaseManager {

    // The "BusyRestaurantPU" must match the name in your persistence.xml
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("BusyRestaurantPU");

    /**
     * Call this method whenever you need to perform a database operation
     * (Save, Delete, or Update).
     */
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Closes the factory when the application shuts down.
     */
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}