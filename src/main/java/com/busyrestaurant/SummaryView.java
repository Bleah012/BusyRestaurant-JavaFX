package com.busyrestaurant;

import com.busyrestaurant.model.MenuItem;
import com.busyrestaurant.model.Order;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SummaryView {

    private static FlowPane summaryGrid;

    public static void show(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8f9fa;");

        // --- TOP NAVIGATION BAR (Matching KitchenView) ---
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(15, 30, 15, 30));
        topBar.setStyle("-fx-background-color: #262626;");

        Label brandTitle = new Label("Active Items Summary");
        brandTitle.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnTimeline = new Button("Timeline View");
        btnTimeline.setStyle("-fx-background-color: #3f3f3f; -fx-text-fill: #b3b3b3; -fx-background-radius: 5; -fx-padding: 8 15; -fx-cursor: hand;");
        btnTimeline.setOnAction(e -> KitchenView.show(stage)); // Go back to cards

        Button btnSummary = new Button("Summary View");
        btnSummary.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 8 15;");

        topBar.getChildren().addAll(brandTitle, spacer, btnTimeline, btnSummary);

        // --- SUMMARY GRID ---
        summaryGrid = new FlowPane();
        summaryGrid.setHgap(20);
        summaryGrid.setVgap(20);
        summaryGrid.setPadding(new Insets(30));

        refreshSummary();

        ScrollPane scroll = new ScrollPane(summaryGrid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        root.setTop(topBar);
        root.setCenter(scroll);

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    private static void refreshSummary() {
        summaryGrid.getChildren().clear();

        // Logic to count items across all active orders
        Map<String, Integer> itemCounts = new HashMap<>();

        for (Order order : KitchenManager.getInstance().getActiveOrders()) {
            for (MenuItem item : order.getItems()) {
                itemCounts.put(item.getName(), itemCounts.getOrDefault(item.getName(), 0) + 1);
            }
        }

        // Create a card for each unique item
        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            summaryGrid.getChildren().add(createSummaryCard(entry.getKey(), entry.getValue()));
        }
    }

    private static VBox createSummaryCard(String itemName, int count) {
        VBox card = new VBox(5);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setPrefSize(220, 120);

        card.setStyle("-fx-background-color: #333; " +
                "-fx-background-radius: 12; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 4);");

        Label countLabel = new Label(String.valueOf(count));
        countLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #e67e22;");

        Label nameLabel = new Label(itemName.toUpperCase());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #b3b3b3; -fx-font-weight: bold;");

        card.getChildren().addAll(countLabel, nameLabel);
        return card;
    }
}