package com.busyrestaurant;

import com.busyrestaurant.model.MenuItem;
import com.busyrestaurant.model.Order;
import com.busyrestaurant.network.OrderClient;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class MenuView {

    private static FlowPane foodGrid;

    public static void show(Stage stage) {
        BorderPane root = new BorderPane();

        // 1. Sidebar (Left) - Category Navigation
        VBox sidebar = new VBox(10);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(200);

        Label brandLabel = new Label("MENU");
        brandLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 0 0 20 0;");

        Button btnAppetizers = createCategoryButton("Appetizers", false);
        Button btnMains = createCategoryButton("Main Courses", true);
        Button btnDrinks = createCategoryButton("Drinks", false);
        Button btnDesserts = createCategoryButton("Desserts", false);

        btnAppetizers.setOnAction(e -> filterMenu("Appetizers", btnAppetizers));
        btnMains.setOnAction(e -> filterMenu("Main Courses", btnMains));
        btnDrinks.setOnAction(e -> filterMenu("Drinks", btnDrinks));
        btnDesserts.setOnAction(e -> filterMenu("Desserts", btnDesserts));

        sidebar.getChildren().addAll(brandLabel, btnAppetizers, btnMains, btnDrinks, btnDesserts);

        // 2. Menu Content (Center) - The Food Grid
        foodGrid = new FlowPane();
        foodGrid.setHgap(25);
        foodGrid.setVgap(25);
        foodGrid.setPadding(new Insets(25));
        foodGrid.getStyleClass().add("menu-grid");

        updateGrid("Main Courses");

        ScrollPane scrollPane = new ScrollPane(foodGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:transparent;");

        // 3. Cart Sidebar (Right) - Order Tracking
        VBox cartArea = new VBox(20);
        cartArea.getStyleClass().add("cart-sidebar");
        cartArea.setPrefWidth(320);
        cartArea.setPadding(new Insets(20));

        Label cartTitle = new Label("Your Order");
        cartTitle.getStyleClass().add("cart-title");

        VBox cartItemsList = new VBox(10);
        VBox.setVgrow(cartItemsList, Priority.ALWAYS);
        cartItemsList.getChildren().add(new Label("No items added yet..."));

        VBox cartFooter = new VBox(15);
        cartFooter.setAlignment(Pos.CENTER);

        Label totalLabel = new Label("Total: $0.00");
        totalLabel.getStyleClass().add("total-label");

        // Dynamic Cart Listener
        CartManager.getInstance().getCartItems().addListener((ListChangeListener<MenuItem>) c -> {
            cartItemsList.getChildren().clear();
            for (MenuItem item : CartManager.getInstance().getCartItems()) {
                HBox row = new HBox(10);
                row.getStyleClass().add("cart-item-row");
                row.setAlignment(Pos.CENTER_LEFT);
                Label name = new Label(item.getName());
                Label price = new Label("$" + String.format("%.2f", item.getPrice()));
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                row.getChildren().addAll(name, spacer, price);
                cartItemsList.getChildren().add(row);
            }
            totalLabel.setText("Total: $" + String.format("%.2f", CartManager.getInstance().calculateTotal()));
            if (CartManager.getInstance().getCartItems().isEmpty()) {
                cartItemsList.getChildren().add(new Label("No items added yet..."));
            }
        });

        Button checkoutBtn = new Button("PLACE ORDER");
        checkoutBtn.getStyleClass().add("checkout-button");
        checkoutBtn.setMaxWidth(Double.MAX_VALUE);

        // --- UPDATED CHECKOUT LOGIC WITH DYNAMIC TABLE NUMBER ---
        checkoutBtn.setOnAction(e -> {
            if (!CartManager.getInstance().getCartItems().isEmpty()) {
                // Using AppSettings.getTableNumber() instead of hardcoded string
                Order newOrder = new Order("ORD-" + System.currentTimeMillis(), AppSettings.getTableNumber());

                for (MenuItem item : CartManager.getInstance().getCartItems()) {
                    newOrder.addItem(item);
                }

                // Networking: Send to the Background Server
                OrderClient.sendOrder(newOrder);

                // Kitchen Sync: Add directly to the KitchenManager
                KitchenManager.getInstance().addOrder(newOrder);

                // UI Clean up
                CartManager.getInstance().clearCart();
                System.out.println("Order Sent from " + AppSettings.getTableNumber() + " successfully!");
            }
        });

        cartFooter.getChildren().addAll(totalLabel, checkoutBtn);
        cartArea.getChildren().addAll(cartTitle, cartItemsList, cartFooter);

        root.setLeft(sidebar);
        root.setCenter(scrollPane);
        root.setRight(cartArea);

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(MenuView.class.getResource("/com/busyrestaurant/css/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setFullScreen(true);
    }

    private static void filterMenu(String category, Button activeBtn) {
        VBox sidebar = (VBox) activeBtn.getParent();
        sidebar.getChildren().forEach(node -> node.getStyleClass().remove("category-button-active"));
        activeBtn.getStyleClass().add("category-button-active");
        updateGrid(category);
    }

    private static void updateGrid(String category) {
        foodGrid.getChildren().clear();
        List<MenuItem> filtered = MenuManager.getInstance().getAllItems().stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());

        for (MenuItem item : filtered) {
            foodGrid.getChildren().add(new FoodCard(item));
        }
    }

    private static Button createCategoryButton(String text, boolean isActive) {
        Button btn = new Button(text);
        btn.getStyleClass().add("category-button");
        if (isActive) {
            btn.getStyleClass().add("category-button-active");
        }
        return btn;
    }
}