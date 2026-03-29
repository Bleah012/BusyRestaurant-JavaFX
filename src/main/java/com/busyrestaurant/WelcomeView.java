package com.busyrestaurant;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WelcomeView {

    public static void show(Stage stage) {
        // 1. The Main Container (StackPane allows layering)
        StackPane root = new StackPane();
        root.getStyleClass().add("root");

        // 2. The Center Content (Title and Order Button)
        VBox centerContent = new VBox(30);
        centerContent.setAlignment(Pos.CENTER);

        Label title = new Label("Welcome to BusyRestaurant");
        title.getStyleClass().add("welcome-title");

        Button startButton = new Button("ORDER NOW");
        startButton.getStyleClass().add("primary-button");
        startButton.setPrefSize(250, 60); // Ensure it's a good size
        startButton.setOnAction(e -> MenuView.show(stage));

        centerContent.getChildren().addAll(title, startButton);

        // 3. The Staff Login Button (Pinned to Bottom Right)
        Button adminLoginBtn = new Button("Staff Login");
        // We make it subtle: white text with low opacity
        // --- REPLACED SECTION START ---
        adminLoginBtn.setStyle(
                "-fx-background-color: #e67e22; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 13px; " +
                        "-fx-padding: 10 20 10 20; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
        );

// Hover effect for better visibility
        adminLoginBtn.setOnMouseEntered(e -> adminLoginBtn.setStyle(
                "-fx-background-color: #d35400; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-font-size: 13px; -fx-padding: 10 20 10 20; -fx-background-radius: 5; -fx-cursor: hand;"
        ));

        adminLoginBtn.setOnMouseExited(e -> adminLoginBtn.setStyle(
                "-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-font-size: 13px; -fx-padding: 10 20 10 20; -fx-background-radius: 5; -fx-cursor: hand;"
        ));

        adminLoginBtn.setOnAction(e -> LoginView.show(stage));
// --- REPLACED SECTION END ---
        // Align the button to the bottom right corner
        StackPane.setAlignment(adminLoginBtn, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(adminLoginBtn, new Insets(20)); // Keep it away from the very edge

        // Add everything to the root
        root.getChildren().addAll(centerContent, adminLoginBtn);

        Scene scene = new Scene(root, 1024, 768);

        // Link the CSS file
        String cssPath = "/com/busyrestaurant/css/style.css";
        if (WelcomeView.class.getResource(cssPath) != null) {
            scene.getStylesheets().add(WelcomeView.class.getResource(cssPath).toExternalForm());
        }

        stage.setTitle("BusyRestaurant - Welcome");
        stage.setScene(scene);

        // Tablet Simulation
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        stage.show();
    }
}