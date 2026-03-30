package com.busyrestaurant.model;

/**
 * Model class representing a food or drink item in the BusyRestaurant system.
 * Updated to support dynamic image path updates from the Admin Dashboard.
 */
public class MenuItem {
    private String id;
    private String name;
    private String description;
    private double price;
    private String category;
    private String imagePath; // Stores the URL or local file path of the food image

    public MenuItem(String id, String name, String description, double price, String category, String imagePath) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imagePath = imagePath;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public String getImagePath() { return imagePath; }

    // --- NEW SETTER ---
    /**
     * Allows the Admin to update the image reference after the object is created.
     * Useful for the FileChooser implementation in AdminView.
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}