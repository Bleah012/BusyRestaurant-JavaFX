package com.busyrestaurant;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {

    public static void show(Stage stage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.getStyleClass().add("login-root");
        root.setStyle("-fx-background-color: #2c3e50;"); // Deep blue matching your brand

        Label title = new Label("ADMIN ACCESS");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Input Fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);
        usernameField.setPrefHeight(40);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);
        passwordField.setPrefHeight(40);

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");

        Button loginBtn = new Button("LOGIN");
        loginBtn.getStyleClass().add("checkout-button"); // Reuse your existing CSS class
        loginBtn.setPrefWidth(300);
        loginBtn.setPrefHeight(40);
        loginBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-cursor: hand;");

        // Logic to verify credentials
        loginBtn.setOnAction(e -> {
            String user = usernameField.getText();
            String pass = passwordField.getText();

            // Simple check: Change these to your preferred credentials
            if (user.equals("admin") && pass.equals("1234")) {
                AdminView.show(stage);
            } else {
                errorLabel.setText("Invalid Credentials. Access Denied.");
            }
        });

        Button backBtn = new Button("Back to Welcome");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #bdc3c7; -fx-underline: true; -fx-cursor: hand;");
        backBtn.setOnAction(e -> WelcomeView.show(stage));

        root.getChildren().addAll(title, usernameField, passwordField, errorLabel, loginBtn, backBtn);

        Scene scene = new Scene(root, 1024, 768);
        // Link to your existing style.css for consistent fonts
        scene.getStylesheets().add(LoginView.class.getResource("/com/busyrestaurant/css/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Staff Login");
        stage.show();
    }
}