package com.busyrestaurant.model;

public class MenuItem {
    private String id;
    private String name;
    private String description;
    private double price;
    private String category;
    private String imagePath;

    public MenuItem(String id, String name, String description, double price, String category, String imagePath) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imagePath = imagePath;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public String getImagePath() { return imagePath; }
}