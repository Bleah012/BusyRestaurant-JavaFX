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
import java.util.Optional;

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

        // Updated column header to Kshs
        TableColumn<MenuItem, Double> priceCol = new TableColumn<>("Price (Kshs)");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<MenuItem, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        // New Column to show defined preferences
        TableColumn<MenuItem, String> optCol = new TableColumn<>("Preferences");
        optCol.setCellValueFactory(new PropertyValueFactory<>("customOptions"));

        table.getColumns().addAll(nameCol, priceCol, catCol, optCol);

        // --- 4. FORM (CREATE WITH DYNAMIC PREFERENCES) ---
        VBox formContainer = new VBox(10);
        formContainer.setPadding(new Insets(15));
        formContainer.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        TextField nameField = new TextField();
        nameField.setPromptText("Food Name (e.g. Beef Burger)");

        TextField priceField = new TextField();
        priceField.setPromptText("Price (e.g. 850)");

        ComboBox<String> catBox = new ComboBox<>();
        catBox.getItems().addAll("Appetizers", "Main Courses", "Drinks", "Desserts");
        catBox.setPromptText("Category");

        // NEW FIELD: Admin defines the preferences here
        TextField optionsField = new TextField();
        optionsField.setPromptText("Preferences (comma-separated, e.g. Extra Sauce, No Onions, Spicy)");
        optionsField.setPrefWidth(400);

        // Image Selection Logic
        final String[] selectedPath = {"default_food.png"};
        Label imgLabel = new Label("No image chosen");
        imgLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic;");

        Button browseBtn = new Button("Browse Image...");
        browseBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Food Image");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                selectedPath[0] = file.toURI().toString();
                imgLabel.setText(file.getName());
                imgLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
            }
        });

        Button addBtn = new Button("Add to Menu");
        addBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");

        addBtn.setOnAction(e -> {
            try {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                String category = catBox.getValue();
                String customOptions = optionsField.getText();

                if (name.isEmpty() || category == null) {
                    new Alert(Alert.AlertType.ERROR, "Please fill in all required fields.").show();
                    return;
                }

                MenuItem newItem = new MenuItem("ID-" + System.currentTimeMillis(), name, "", price, category, selectedPath[0]);
                // Save the preferences from the text field to the model
                newItem.setCustomOptions(customOptions);

                MenuManager.getInstance().addItem(newItem);

                // Clear fields
                nameField.clear();
                priceField.clear();
                optionsField.clear();
                imgLabel.setText("No image chosen");
                selectedPath[0] = "default_food.png";

            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Price must be a valid number.").show();
            } catch (Exception ex) {
                System.out.println("Error adding item: " + ex.getMessage());
            }
        });

        // Arrange form in a layout
        HBox topRow = new HBox(10, nameField, priceField, catBox);
        HBox midRow = new HBox(10, new Label("Options:"), optionsField);
        midRow.setAlignment(Pos.CENTER_LEFT);
        HBox bottomRow = new HBox(10, browseBtn, imgLabel, spacer, addBtn);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        formContainer.getChildren().addAll(new Label("Add New Item Details:"), topRow, midRow, bottomRow);

        // --- 5. DELETE LOGIC ---
        Button delBtn = new Button("Delete Selected Item");
        delBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

        delBtn.setOnAction(e -> {
            MenuItem selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Delete Confirmation");
                confirm.setHeaderText("Delete " + selected.getName() + "?");
                confirm.setContentText("This action is permanent.");

                Optional<ButtonType> result = confirm.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    MenuManager.getInstance().removeItem(selected);
                    new Alert(Alert.AlertType.INFORMATION, selected.getName() + " deleted.").show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Select an item to delete.").show();
            }
        });

        root.getChildren().addAll(header, configBox, table, formContainer, delBtn);

        Scene scene = new Scene(root, 1100, 850);
        stage.setTitle("Admin Dashboard - BusyRestaurant");
        stage.setScene(scene);
        stage.show();
    }
}