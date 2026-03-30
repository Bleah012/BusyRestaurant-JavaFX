package com.busyrestaurant;

import com.busyrestaurant.model.MenuItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AdminView {

    public static void show(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f8f9fa;");

        // --- 1. HEADER ---
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Menu Management System");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button kitchenBtn = new Button("KITCHEN DISPLAY");
        kitchenBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;");
        kitchenBtn.setOnAction(e -> KitchenView.show(stage));

        Button logoutBtn = new Button("LOGOUT");
        logoutBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-cursor: hand; -fx-background-radius: 5;");
        logoutBtn.setOnAction(e -> WelcomeView.show(stage));

        header.getChildren().addAll(title, spacer, kitchenBtn, logoutBtn);

        // --- 2. DEVICE CONFIGURATION ---
        HBox configBox = new HBox(15);
        configBox.setAlignment(Pos.CENTER_LEFT);
        configBox.setPadding(new Insets(15));
        configBox.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-radius: 5;");

        Label configLabel = new Label("DEVICE TABLE ID:");
        configLabel.setStyle("-fx-font-weight: bold;");

        TextField tableNumField = new TextField(AppSettings.getTableNumber());
        tableNumField.setPrefWidth(80);

        Button saveConfigBtn = new Button("UPDATE TABLE");
        saveConfigBtn.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-weight: bold;");
        saveConfigBtn.setOnAction(e -> {
            if(!tableNumField.getText().isEmpty()) {
                AppSettings.setTableNumber(tableNumField.getText());
                new Alert(Alert.AlertType.INFORMATION, "Device ID updated to " + tableNumField.getText()).show();
            }
        });

        configBox.getChildren().addAll(configLabel, tableNumField, saveConfigBtn);

        // --- 3. TABLE VIEW ---
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

        // --- 4. FORM (CREATE WITH IMAGE UPLOAD) ---
        VBox formContainer = new VBox(10);
        formContainer.setPadding(new Insets(15));
        formContainer.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");

        HBox inputs = new HBox(10);
        inputs.setAlignment(Pos.CENTER_LEFT);

        TextField nameField = new TextField();
        nameField.setPromptText("Food Name");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        ComboBox<String> catBox = new ComboBox<>();
        catBox.getItems().addAll("Appetizers", "Main Courses", "Drinks", "Desserts");
        catBox.setPromptText("Category");

        // Image Selection Logic
        final String[] selectedPath = {"default_food.png"};
        Label imgLabel = new Label("No image chosen");
        imgLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic;");

        Button browseBtn = new Button("Browse Image...");
        browseBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Food Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                selectedPath[0] = file.toURI().toString(); // Store as URI for JavaFX Image compatibility
                imgLabel.setText(file.getName());
                imgLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
            }
        });

        Button addBtn = new Button("Add to Menu");
        addBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");

        addBtn.setOnAction(e -> {
            try {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                String category = catBox.getValue();

                if (name.isEmpty() || category == null) return;

                MenuItem newItem = new MenuItem("ID-" + System.currentTimeMillis(), name, "", price, category, selectedPath[0]);
                MenuManager.getInstance().addItem(newItem);

                // Clear fields
                nameField.clear();
                priceField.clear();
                imgLabel.setText("No image chosen");
                selectedPath[0] = "default_food.png";
            } catch (Exception ex) {
                System.out.println("Error adding item: " + ex.getMessage());
            }
        });

        inputs.getChildren().addAll(nameField, priceField, catBox, browseBtn, imgLabel, addBtn);
        formContainer.getChildren().addAll(new Label("Add New Item Details:"), inputs);

        // --- 5. DELETE ---
        Button delBtn = new Button("Delete Selected Item");
        delBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        delBtn.setOnAction(e -> {
            MenuItem selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) MenuManager.getInstance().removeItem(selected);
        });

        root.getChildren().addAll(header, configBox, table, formContainer, delBtn);

        Scene scene = new Scene(root, 1100, 800);
        stage.setTitle("Admin Dashboard - BusyRestaurant");
        stage.setScene(scene);
        stage.show();
    }
}