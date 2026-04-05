package com.busyrestaurant;

import com.busyrestaurant.model.Order;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OrderHistoryView {

    public static void show() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("My Order History");

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f9f9f9;");

        Label title = new Label("Recent Orders");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox ordersListUI = new VBox(10);

        // Populate orders from OrderManager
        for (Order order : OrderManager.getInstance().getCustomerOrders()) {
            HBox row = new HBox(15);
            row.setPadding(new Insets(10));
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #ddd;");

            VBox info = new VBox(5);
            Label idLabel = new Label("Order: " + order.getOrderId());
            idLabel.setStyle("-fx-font-weight: bold;");
            Label statusLabel = new Label("Status: " + order.getStatus());
            statusLabel.setStyle("-fx-text-fill: " + (order.getStatus().equals("Cancelled") ? "red" : "#e67e22") + ";");
            info.getChildren().addAll(idLabel, statusLabel);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Button cancelBtn = new Button("Cancel");
            cancelBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-background-radius: 5;");

            // Disable cancel if already ready or cancelled
            if (!order.getStatus().equals("Pending")) {
                cancelBtn.setDisable(true);
                cancelBtn.setOpacity(0.5);
            }

            cancelBtn.setOnAction(e -> {
                order.setStatus("Cancelled");
                KitchenManager.getInstance().removeOrder(order);
                statusLabel.setText("Status: Cancelled");
                cancelBtn.setDisable(true);
            });

            row.getChildren().addAll(info, spacer, cancelBtn);
            ordersListUI.getChildren().add(row);
        }

        ScrollPane scroll = new ScrollPane(ordersListUI);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(400);

        Button closeBtn = new Button("Back to Menu");
        closeBtn.setMaxWidth(Double.MAX_VALUE);
        closeBtn.setOnAction(e -> stage.close());

        layout.getChildren().addAll(title, scroll, closeBtn);
        stage.setScene(new Scene(layout, 450, 550));
        stage.show();
    }
}