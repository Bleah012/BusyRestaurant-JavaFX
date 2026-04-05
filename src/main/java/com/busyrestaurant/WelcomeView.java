package com.busyrestaurant;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class WelcomeView {

    public static void show(Stage stage) {
        // 1. The Main Container
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #f9f9fb;"); // Soft off-white background

        // --- TOP NAVIGATION BAR ---
        HBox navBar = new HBox(15);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(30, 50, 30, 50));

        // Logo Icon (Simulated with a styled Label)
        Label logoIcon = new Label("B");
        logoIcon.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 22px; -fx-padding: 8 15; -fx-background-radius: 10;");

        VBox logoText = new VBox(0);
        Label brandName = new Label("Busy Restaurant");
        brandName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Label brandSub = new Label("Self-Service Ordering");
        brandSub.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");
        logoText.getChildren().addAll(brandName, brandSub);

        Region navSpacer = new Region();
        HBox.setHgrow(navSpacer, Priority.ALWAYS);

        Label tableInfo = new Label("Table Service Available");
        tableInfo.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 13px;");

        navBar.getChildren().addAll(logoIcon, logoText, navSpacer, tableInfo);

        // --- CENTER HERO CONTENT ---
        VBox heroSection = new VBox(25);
        heroSection.setAlignment(Pos.CENTER);
        heroSection.setPadding(new Insets(0, 0, 100, 0)); // Offset upward

        Label badge = new Label("✨ Fast, Easy, Delicious");
        badge.setStyle("-fx-background-color: #fff3e0; -fx-text-fill: #e67e22; -fx-padding: 8 20; " +
                "-fx-background-radius: 20; -fx-font-weight: bold; -fx-font-size: 12px; " +
                "-fx-border-color: #ffe0b2; -fx-border-radius: 20;");

        Label mainTitle = new Label("Welcome to Your\nDining Experience");
        mainTitle.setAlignment(Pos.CENTER);
        mainTitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        mainTitle.setStyle("-fx-font-size: 68px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-line-spacing: -10;");

        Label description = new Label("Browse our carefully curated menu, customize your order, and\nenjoy restaurant-quality meals prepared fresh to order");
        description.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        description.setStyle("-fx-font-size: 18px; -fx-text-fill: #7f8c8d; -fx-line-spacing: 5;");

        // --- YOUR WORKING BUTTON (UNTOUCHED LOGIC) ---
        Button startButton = new Button("ORDER NOW");
        startButton.setPrefSize(280, 70);
        String normalStyle = "-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-size: 20px; " +
                "-fx-font-weight: bold; -fx-background-radius: 35; -fx-cursor: hand; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 10, 0, 0, 5);";
        String hoverStyle = "-fx-background-color: #d35400; -fx-text-fill: white; -fx-font-size: 20px; " +
                "-fx-font-weight: bold; -fx-background-radius: 35; -fx-cursor: hand; " +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;";
        startButton.setStyle(normalStyle);
        startButton.setOnMouseEntered(e -> startButton.setStyle(hoverStyle));
        startButton.setOnMouseExited(e -> startButton.setStyle(normalStyle));
        startButton.setOnAction(e -> MenuView.show(stage));

        heroSection.getChildren().addAll(badge, mainTitle, description, startButton);

        // --- BOTTOM FEATURE CARDS ---
        HBox features = new HBox(30);
        features.setAlignment(Pos.CENTER);
        features.setPadding(new Insets(0, 0, 80, 0));

        features.getChildren().addAll(
                createFeatureCard("🍳", "Fresh Ingredients", "Locally sourced, daily\nprepared"),
                createFeatureCard("🕒", "Quick Service", "Average 15-20 minute\nwait"),
                createFeatureCard("✨", "Customizable", "Made exactly how you\nlike it")
        );

        // --- STAFF LOGIN ---
        Button adminLoginBtn = new Button("Staff Login");
        adminLoginBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #bdc3c7; -fx-font-size: 12px; -fx-cursor: hand;");
        adminLoginBtn.setOnAction(e -> LoginView.show(stage));
        StackPane.setAlignment(adminLoginBtn, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(adminLoginBtn, new Insets(20));

        // Final Layout Assembly
        VBox layoutWrapper = new VBox();
        layoutWrapper.getChildren().addAll(navBar, heroSection, features);
        root.getChildren().addAll(layoutWrapper, adminLoginBtn);

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    // Helper method to create those clean cards from your image
    private static VBox createFeatureCard(String icon, String title, String desc) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(250, 180);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 15, 0, 0, 5); " +
                "-fx-border-color: #f1f2f6; -fx-border-radius: 20;");

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 24px; -fx-background-color: #fff3e0; -fx-padding: 10; -fx-background-radius: 12;");

        Label t = new Label(title);
        t.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-font-size: 14px;");

        Label d = new Label(desc);
        d.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        d.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 12px;");

        card.getChildren().addAll(iconLabel, t, d);
        return card;
    }
}