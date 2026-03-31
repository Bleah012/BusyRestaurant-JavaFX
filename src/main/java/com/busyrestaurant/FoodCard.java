package com.busyrestaurant;

import com.busyrestaurant.model.MenuItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class FoodCard extends VBox {

    public FoodCard(MenuItem item) {
        // Spacing between elements
        super(12);
        this.setPadding(new Insets(0, 0, 15, 0));
        this.setPrefWidth(280);
        this.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        // --- 1. IMAGE SECTION ---
        ImageView imageView = new ImageView();
        imageView.setFitWidth(280);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(false); // To fill the top area like your screenshot

        // Clip the image to have rounded top corners
        Rectangle clip = new Rectangle(280, 180);
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        imageView.setClip(clip);

        try {
            String path = item.getImagePath();
            Image img;
            if (path.startsWith("file:") || path.startsWith("http")) {
                img = new Image(path, true);
            } else {
                img = new Image(getClass().getResourceAsStream("/com/busyrestaurant/images/" + path));
            }
            imageView.setImage(img);
        } catch (Exception e) {
            // Fallback image handling
            imageView.setStyle("-fx-background-color: #ddd;");
        }

        // --- 2. TEXT CONTENT ---
        VBox textContent = new VBox(5);
        textContent.setPadding(new Insets(0, 15, 0, 15));

        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label descLabel = new Label("Freshly prepared " + item.getCategory().toLowerCase());
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #777;");

        textContent.getChildren().addAll(nameLabel, descLabel);

        // --- 3. BOTTOM BAR (Price & Add Button) ---
        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setPadding(new Insets(0, 15, 0, 15));

        // UPDATED: Changed to Kshs.
        Label priceLabel = new Label("Kshs. " + String.format("%.0f", item.getPrice()));
        priceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #e67e22;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // UPDATED: Styled button to match your Orange theme
        Button addButton = new Button("+ Add");
        addButton.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-padding: 8 20; -fx-cursor: hand;");

        // STEP 3: Action now opens the Customization Dialog instead of adding immediately
        addButton.setOnAction(e -> {
            MenuView.showCustomizeDialog(item);
        });

        bottomBar.getChildren().addAll(priceLabel, spacer, addButton);

        // Add all components to the card
        this.getChildren().addAll(imageView, textContent, bottomBar);
    }
}