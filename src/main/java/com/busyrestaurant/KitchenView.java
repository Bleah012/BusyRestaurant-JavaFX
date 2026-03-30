package com.busyrestaurant;

import com.busyrestaurant.model.MenuItem;
import com.busyrestaurant.model.Order;
import com.busyrestaurant.network.OrderServer; // Ensure this is imported
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class KitchenView {

    private static FlowPane ordersGrid;

    public static void show(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8f9fa;");

        // --- 1. TOP NAVIGATION BAR ---
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(15, 30, 15, 30));
        topBar.setStyle("-fx-background-color: #262626;");

        Label brandTitle = new Label("Restaurant Self-Ordering System");
        brandTitle.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnTimeline = new Button("Timeline View");
        btnTimeline.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 8 15; -fx-cursor: hand;");

        Button btnSummary = new Button("Summary View");
        btnSummary.setStyle("-fx-background-color: #3f3f3f; -fx-text-fill: #b3b3b3; -fx-background-radius: 5; -fx-padding: 8 15; -fx-cursor: hand;");

        // Navigation to Summary
        btnSummary.setOnAction(e -> SummaryView.show(stage));

        topBar.getChildren().addAll(brandTitle, spacer, btnTimeline, btnSummary);

        // --- 2. ORDERS GRID ---
        ordersGrid = new FlowPane();
        ordersGrid.setHgap(20);
        ordersGrid.setVgap(20);
        ordersGrid.setPadding(new Insets(30));

        ScrollPane scroll = new ScrollPane(ordersGrid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // --- 3. SERVER & DYNAMIC SYNC ---

        // Start the server to listen for incoming network orders
        // We only start it if it hasn't been started yet to avoid port conflicts
        OrderServer.startServer(order -> {
            Platform.runLater(() -> {
                // Add the order to the manager, which triggers the listener below
                KitchenManager.getInstance().addOrder(order);
            });
        });

        // UI Listener: Redraws the grid whenever the KitchenManager list changes
        KitchenManager.getInstance().getActiveOrders().addListener((ListChangeListener<Order>) c -> {
            Platform.runLater(KitchenView::refreshGrid);
        });

        refreshGrid();

        root.setTop(topBar);
        root.setCenter(scroll);

        Scene scene = new Scene(root, 1200, 800);
        if (KitchenView.class.getResource("/com/busyrestaurant/css/style.css") != null) {
            scene.getStylesheets().add(KitchenView.class.getResource("/com/busyrestaurant/css/style.css").toExternalForm());
        }

        stage.setTitle("Kitchen Display System - Timeline");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    private static void refreshGrid() {
        ordersGrid.getChildren().clear();
        for (Order order : KitchenManager.getInstance().getActiveOrders()) {
            ordersGrid.getChildren().add(createOrderCard(order));
        }
    }

    private static VBox createOrderCard(Order order) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setPrefWidth(300);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4);");

        HBox cardHeader = new HBox();
        Label tableLabel = new Label(order.getTableNumber());
        tableLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Region hSpacer = new Region();
        HBox.setHgrow(hSpacer, Priority.ALWAYS);

        Label timeLabel = new Label("🕒 2 min");
        timeLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12px;");
        cardHeader.getChildren().addAll(tableLabel, hSpacer, timeLabel);

        Label statusBadge = new Label(order.getStatus().toUpperCase());
        statusBadge.setStyle("-fx-background-color: #f1f3f5; -fx-text-fill: #6c757d; -fx-padding: 4 10; " +
                "-fx-background-radius: 15; -fx-font-size: 11px; -fx-font-weight: bold;");

        Separator sep = new Separator();
        sep.setPadding(new Insets(5, 0, 5, 0));

        VBox itemsBox = new VBox(8);
        for (MenuItem item : order.getItems()) {
            Label itemLabel = new Label("• " + item.getName());
            itemLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2c3e50;");
            itemsBox.getChildren().add(itemLabel);
        }

        Button completeBtn = new Button("Complete");
        completeBtn.setMaxWidth(Double.MAX_VALUE);
        completeBtn.setPrefHeight(45);
        completeBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-cursor: hand;");

        completeBtn.setOnAction(e -> KitchenManager.getInstance().removeOrder(order));

        card.getChildren().addAll(cardHeader, statusBadge, sep, itemsBox, completeBtn);
        return card;
    }
}