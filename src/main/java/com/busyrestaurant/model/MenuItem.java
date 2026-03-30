package com.busyrestaurant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

/**
 * Model class representing a food or drink item.
 * Annotated for JPA persistence in the H2/MySQL database.
 */
@Entity
@Table(name = "menu_items")
public class MenuItem {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    private String description;
    private double price;
    private String category;

    @Column(name = "image_path", length = 512)
    private String imagePath;

    /**
     * IMPORTANT: JPA requires a no-argument constructor.
     * The database uses this to instantiate the object before filling it with data.
     */
    public MenuItem() {
    }

    public MenuItem(String id, String name, String description, double price, String category, String imagePath) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imagePath = imagePath;
    }

    // --- GETTERS ---
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public String getImagePath() { return imagePath; }

    // --- SETTERS ---
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}