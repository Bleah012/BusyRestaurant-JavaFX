package com.busyrestaurant;

import com.busyrestaurant.model.MenuItem;
import com.busyrestaurant.model.Order;
import com.busyrestaurant.network.OrderServer;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class KitchenView {

    public static void show(Stage stage) {
        // The main container for all incoming orders
        FlowPane ordersGrid = new FlowPane();
        ordersGrid.setHgap(20);
        ordersGrid.setVgap(20);
        ordersGrid.setPadding(new Insets(20));
        ordersGrid.getStyleClass().add("kitchen-root");

        // Start the server and define what happens when an order arrives
        OrderServer.startServer(order -> {
            // This code runs inside Platform.runLater (from our OrderServer update)
            VBox orderCard = createOrderCard(order, ordersGrid);
            ordersGrid.getChildren().add(orderCard);
        });

        ScrollPane scroll = new ScrollPane(ordersGrid);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setStyle("-fx-background-color: #2c3e50;"); // Match the kitchen theme

        Scene scene = new Scene(scroll, 1024, 768);

        // Ensure your CSS is linked
        if (KitchenView.class.getResource("/com/busyrestaurant/css/style.css") != null) {
            scene.getStylesheets().add(KitchenView.class.getResource("/com/busyrestaurant/css/style.css").toExternalForm());
        }

        stage.setTitle("Kitchen Display System (KDS)");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates a visual card for a single order.
     */
    private static VBox createOrderCard(Order order, FlowPane parent) {
        VBox card = new VBox(10);
        card.getStyleClass().add("order-card");
        card.setMinWidth(250);

        // Header: Table Number and Order ID
        Label header = new Label("TABLE: " + order.getTableNumber());
        header.getStyleClass().add("order-header");
        header.setStyle("-fx-text-fill: #e67e22; -fx-font-size: 18px; -fx-font-weight: bold;");

        // List of items in the order
        VBox itemsBox = new VBox(5);
        for (MenuItem item : order.getItems()) {
            Label itemLabel = new Label("• " + item.getName());
            itemLabel.setStyle("-fx-font-size: 14px;");
            itemsBox.getChildren().add(itemLabel);
        }

        // "Complete" button to remove the order once cooked
        Button doneBtn = new Button("MARK AS DONE");
        doneBtn.getStyleClass().add("done-button");
        doneBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand;");
        doneBtn.setMaxWidth(Double.MAX_VALUE);

        doneBtn.setOnAction(e -> parent.getChildren().remove(card));

        card.getChildren().addAll(header, itemsBox, doneBtn);
        return card;
    }
}