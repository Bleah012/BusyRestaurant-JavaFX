package com.busyrestaurant.model;
import java.util.List;
import java.util.ArrayList;

public class Order {
    private String orderId;
    private String tableNumber;
    private List<MenuItem> items;
    private String status;
    private long timestamp;

    public Order(String orderId, String tableNumber) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.items = new ArrayList<>();
        this.status = "Pending"; // Matches #9ca3af in your design
        this.timestamp = System.currentTimeMillis();
    }

    public void addItem(MenuItem item) { this.items.add(item); }
    public String getOrderId() { return orderId; }
    public String getTableNumber() { return tableNumber; }
    public List<MenuItem> getItems() { return items; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}