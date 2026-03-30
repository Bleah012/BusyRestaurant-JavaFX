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
        super(10);
        this.getStyleClass().add("food-card");
        this.setAlignment(Pos.CENTER);

        // 1. Image Container
        StackPane imageContainer = new StackPane();
        imageContainer.getStyleClass().add("food-image-box");
        imageContainer.setPrefSize(180, 140);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(160);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        try {
            String path = item.getImagePath();
            Image img;

            // CHECK: Is this an external file URI (from Admin Upload) or an internal resource?
            if (path.startsWith("file:") || path.startsWith("http")) {
                // Load external file
                img = new Image(path, true); // 'true' enables background loading
            } else {
                // Load from internal resources: src/main/resources/com/busyrestaurant/images/
                String resourcePath = "/com/busyrestaurant/images/" + path;
                img = new Image(getClass().getResourceAsStream(resourcePath));
            }

            imageView.setImage(img);
            imageContainer.getChildren().add(imageView);

        } catch (Exception e) {
            // Fallback for missing images
            Label errorLabel = new Label("Image Not Found");
            errorLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");
            imageContainer.getChildren().add(errorLabel);
            System.err.println("Could not load image path: " + item.getImagePath());
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

        this.getChildren().addAll(imageContainer, nameLabel, priceLabel, addButton);
    }
}