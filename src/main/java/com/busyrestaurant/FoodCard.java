package com.busyrestaurant;

import com.busyrestaurant.model.MenuItem;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class FoodCard extends VBox {

    public FoodCard(MenuItem item) {
        // Set spacing between image, name, and price
        super(10);
        this.getStyleClass().add("food-card");
        this.setAlignment(Pos.CENTER);

        // 1. Image Container
        StackPane imageContainer = new StackPane();
        imageContainer.getStyleClass().add("food-image-box");
        imageContainer.setPrefSize(180, 140);

        try {
            // Load image from: src/main/resources/com/busyrestaurant/images/
            String imagePath = "/com/busyrestaurant/images/" + item.getImagePath();
            Image img = new Image(getClass().getResourceAsStream(imagePath));

            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(160);
            imageView.setFitHeight(120);
            imageView.setPreserveRatio(true);

            imageContainer.getChildren().add(imageView);
        } catch (Exception e) {
            // Fallback if the file (burger.jpg, etc.) is missing or misnamed
            Label errorLabel = new Label("Image Not Found");
            errorLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");
            imageContainer.getChildren().add(errorLabel);
            System.err.println("Could not load image: " + item.getImagePath());
        }

        // 2. Name and Price
        Label nameLabel = new Label(item.getName());
        nameLabel.getStyleClass().add("food-name");

        Label priceLabel = new Label("$" + String.format("%.2f", item.getPrice()));
        priceLabel.getStyleClass().add("food-price");

        // 3. Add to Cart Button
        Button addButton = new Button("+ Add to Cart");
        addButton.getStyleClass().add("add-button");
        addButton.setPrefWidth(150);

        addButton.setOnAction(e -> {
            CartManager.getInstance().addItem(item);
            System.out.println("Added to cart: " + item.getName());
        });

        // Add all elements to this VBox
        this.getChildren().addAll(imageContainer, nameLabel, priceLabel, addButton);
    }
}