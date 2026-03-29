package com.busyrestaurant;

import com.busyrestaurant.model.MenuItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AdminView {

    public static void show(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f8f9fa;");

        // --- HEADER (Title + Logout Button) ---
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Menu Management System");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Spacer to push the logout button to the far right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = new Button("LOGOUT");
        logoutBtn.setStyle(
                "-fx-background-color: #e67e22; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8 20 8 20; " +
                        "-fx-cursor: hand; " +
                        "-fx-background-radius: 5;"
        );
        // Action: Return to Welcome Screen
        logoutBtn.setOnAction(e -> WelcomeView.show(stage));

        header.getChildren().addAll(title, spacer, logoutBtn);

        // --- TABLE (READ) ---
        TableView<MenuItem> table = new TableView<>();
        table.setItems(MenuManager.getInstance().getAllItems());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<MenuItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<MenuItem, Double> priceCol = new TableColumn<>("Price ($)");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<MenuItem, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        table.getColumns().addAll(nameCol, priceCol, catCol);

        // --- FORM (CREATE) ---
        HBox form = new HBox(15);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setPadding(new Insets(10));
        form.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");

        TextField nameField = new TextField();
        nameField.setPromptText("Food Name");

        TextField priceField = new TextField();
        priceField.setPromptText("Price (e.g. 10.50)");

        ComboBox<String> catBox = new ComboBox<>();
        catBox.getItems().addAll("Appetizers", "Main Courses", "Drinks", "Desserts");
        catBox.setPromptText("Category");

        Button addBtn = new Button("Add to Menu");
        addBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");

        addBtn.setOnAction(e -> {
            try {
                String id = "ID-" + System.currentTimeMillis();
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                String category = catBox.getValue();

                if (name.isEmpty() || category == null) return;

                MenuItem newItem = new MenuItem(id, name, "", price, category, "burger.jpg");
                MenuManager.getInstance().addItem(newItem);

                nameField.clear();
                priceField.clear();
            } catch (NumberFormatException ex) {
                System.out.println("Invalid price entered.");
            }
        });

        form.getChildren().addAll(nameField, priceField, catBox, addBtn);

        // --- DELETE SECTION ---
        Button delBtn = new Button("Delete Selected Item");
        delBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        delBtn.setOnAction(e -> {
            MenuItem selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                MenuManager.getInstance().removeItem(selected);
            }
        });

        // Add everything to root (Header added here)
        root.getChildren().addAll(header, table, new Label("Add New Item:"), form, delBtn);

        Scene scene = new Scene(root, 900, 650);
        stage.setTitle("Admin Dashboard - BusyRestaurant");
        stage.setScene(scene);
        stage.show();
    }
}