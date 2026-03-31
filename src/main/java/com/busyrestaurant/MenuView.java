package com.busyrestaurant;

import com.busyrestaurant.model.MenuItem;
import com.busyrestaurant.model.Order;
import com.busyrestaurant.network.OrderClient;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class MenuView {

    private static FlowPane foodGrid;

    public static void show(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f4f4;");

        // --- 1. SIDEBAR (Left) ---
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(30, 20, 30, 20));
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: white;");

        Label brandLabel = new Label("Menu");
        brandLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Added "All" button and set it as active by default
        Button btnAll = createCategoryButton("All", true);
        Button btnAppetizers = createCategoryButton("Appetizers", false);
        Button btnMains = createCategoryButton("Main Courses", false);
        Button btnDrinks = createCategoryButton("Drinks", false);
        Button btnDesserts = createCategoryButton("Desserts", false);

        btnAll.setOnAction(e -> filterMenu("All", btnAll));
        btnAppetizers.setOnAction(e -> filterMenu("Appetizers", btnAppetizers));
        btnMains.setOnAction(e -> filterMenu("Main Courses", btnMains));
        btnDrinks.setOnAction(e -> filterMenu("Drinks", btnDrinks));
        btnDesserts.setOnAction(e -> filterMenu("Desserts", btnDesserts));

        sidebar.getChildren().addAll(brandLabel, btnAll, btnAppetizers, btnMains, btnDrinks, btnDesserts);

        // --- 2. MENU CONTENT (Center) ---
        foodGrid = new FlowPane();
        foodGrid.setHgap(20);
        foodGrid.setVgap(20);
        foodGrid.setPadding(new Insets(30));

        // Initial Load: Show everything
        updateGrid("All");

        ScrollPane scrollPane = new ScrollPane(foodGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        // --- 3. CART SIDEBAR (Right) ---
        VBox cartArea = new VBox(15);
        cartArea.setPrefWidth(350);
        cartArea.setPadding(new Insets(30, 20, 30, 20));
        cartArea.setStyle("-fx-background-color: white; -fx-border-color: #eee; -fx-border-width: 0 0 0 1;");

        Label cartTitle = new Label("My Order");
        cartTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox cartItemsList = new VBox(15);
        VBox.setVgrow(cartItemsList, Priority.ALWAYS);
        cartItemsList.getChildren().add(new Label("No items added yet..."));

        VBox summaryBox = new VBox(8);
        summaryBox.setPadding(new Insets(20, 0, 10, 0));
        summaryBox.setStyle("-fx-border-color: #eee; -fx-border-width: 1 0 0 0;");

        Label subtotalLabel = new Label("Subtotal: Kshs. 0.00");
        Label taxLabel = new Label("Tax (16%): Kshs. 0.00");
        Label totalLabel = new Label("Total: Kshs. 0.00");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #e67e22;");

        Button checkoutBtn = new Button("Place Order");
        checkoutBtn.setMaxWidth(Double.MAX_VALUE);
        checkoutBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");

        CartManager.getInstance().getCartItems().addListener((ListChangeListener<MenuItem>) c -> {
            cartItemsList.getChildren().clear();
            double subtotal = CartManager.getInstance().calculateTotal();
            double tax = subtotal * 0.16;
            double total = subtotal + tax;

            for (MenuItem item : CartManager.getInstance().getCartItems()) {
                HBox row = new HBox(10);
                row.setAlignment(Pos.CENTER_LEFT);
                Label name = new Label(item.getName());
                Label price = new Label("Kshs. " + String.format("%.0f", item.getPrice()));
                price.setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;");
                Region s = new Region();
                HBox.setHgrow(s, Priority.ALWAYS);
                row.getChildren().addAll(name, s, price);
                cartItemsList.getChildren().add(row);
            }

            subtotalLabel.setText("Subtotal: Kshs. " + String.format("%.2f", subtotal));
            taxLabel.setText("Tax (16%): Kshs. " + String.format("%.2f", tax));
            totalLabel.setText("Total: Kshs. " + String.format("%.2f", total));

            if (CartManager.getInstance().getCartItems().isEmpty()) {
                cartItemsList.getChildren().add(new Label("No items added yet..."));
            }
        });

        checkoutBtn.setOnAction(e -> {
            if (!CartManager.getInstance().getCartItems().isEmpty()) {
                Order newOrder = new Order("ORD-" + System.currentTimeMillis(), AppSettings.getTableNumber());
                newOrder.getItems().addAll(CartManager.getInstance().getCartItems());
                OrderClient.sendOrder(newOrder);
                KitchenManager.getInstance().addOrder(newOrder);
                CartManager.getInstance().clearCart();
                new Alert(Alert.AlertType.INFORMATION, "Order sent to kitchen!").show();
            }
        });

        summaryBox.getChildren().addAll(subtotalLabel, taxLabel, totalLabel);
        cartArea.getChildren().addAll(cartTitle, cartItemsList, summaryBox, checkoutBtn);

        root.setLeft(sidebar);
        root.setCenter(scrollPane);
        root.setRight(cartArea);

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public static void showCustomizeDialog(MenuItem item) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Customize " + item.getName());

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: white;");

        Label title = new Label("Customize Your Order");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox details = new VBox(5);
        Label name = new Label(item.getName());
        name.setStyle("-fx-font-size: 18px; -fx-text-fill: #333;");
        Label price = new Label("Kshs. " + String.format("%.0f", item.getPrice()));
        price.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #e67e22;");
        details.getChildren().addAll(name, price);

        GridPane modifierGrid = new GridPane();
        modifierGrid.setHgap(10);
        modifierGrid.setVgap(10);

        String optionsStr = item.getCustomOptions();
        if (optionsStr != null && !optionsStr.trim().isEmpty()) {
            String[] options = optionsStr.split(",");
            for (int i = 0; i < options.length; i++) {
                ToggleButton tb = new ToggleButton(options[i].trim());
                tb.setPrefWidth(160);
                tb.setPrefHeight(40);
                tb.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-cursor: hand;");

                tb.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal) tb.setStyle("-fx-background-color: #fff3e0; -fx-border-color: #e67e22; -fx-border-radius: 5;");
                    else tb.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
                });

                modifierGrid.add(tb, i % 2, i / 2);
            }
        } else {
            modifierGrid.add(new Label("No customizations for this item."), 0, 0);
        }

        Button addBtn = new Button("Add to Order");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setPadding(new Insets(12));
        addBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");

        addBtn.setOnAction(e -> {
            CartManager.getInstance().addItem(item);
            dialog.close();
        });

        layout.getChildren().addAll(title, details, new Label("Select Preferences:"), modifierGrid, addBtn);

        dialog.setScene(new Scene(layout, 400, 500));
        dialog.showAndWait();
    }

    private static void filterMenu(String category, Button activeBtn) {
        VBox sidebar = (VBox) activeBtn.getParent();
        sidebar.getChildren().forEach(n -> n.setStyle("-fx-background-color: transparent; -fx-text-fill: #555;"));
        activeBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-background-radius: 8;");
        updateGrid(category);
    }

    private static void updateGrid(String category) {
        foodGrid.getChildren().clear();

        List<MenuItem> allItems = MenuManager.getInstance().getAllItems();
        List<MenuItem> filtered;

        if ("All".equalsIgnoreCase(category)) {
            filtered = allItems;
        } else {
            filtered = allItems.stream()
                    .filter(item -> item.getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }

        for (MenuItem item : filtered) {
            foodGrid.getChildren().add(new FoodCard(item));
        }
    }

    private static Button createCategoryButton(String text, boolean isActive) {
        Button btn = new Button(text);
        btn.setPrefWidth(180);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(10, 15, 10, 15));
        if (isActive) btn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-background-radius: 8;");
        else btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #555;");
        return btn;
    }
}