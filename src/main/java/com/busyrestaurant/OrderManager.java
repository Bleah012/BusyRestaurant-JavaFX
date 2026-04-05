package com.busyrestaurant;

import com.busyrestaurant.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class OrderManager {
    private static OrderManager instance;
    private final ObservableList<Order> customerOrders;

    private OrderManager() {
        customerOrders = FXCollections.observableArrayList();
    }

    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public ObservableList<Order> getCustomerOrders() {
        return customerOrders;
    }

    public void addOrder(Order order) {
        customerOrders.add(order);
    }
}