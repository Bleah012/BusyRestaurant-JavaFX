package com.busyrestaurant;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main Entry Point for the BusyRestaurant Self-Ordering System.
 * This class initializes the JavaFX Primary Stage.
 */
public class BusyRestaurantApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // STEP 4.4 UPDATE:
        // Launch the Kitchen View to start the server and listen for orders
        WelcomeView.show(primaryStage);
        //KitchenView.show(primaryStage);
        //AdminView.show(primaryStage);
        //MenuView.show(primaryStage);

        // Note: Once you confirm the server works, you can switch back
        // to  KitchenView.show(primaryStage);for the customer-facing side.
    }

    public static void main(String[] args) {
        // launch(args) tells the JVM to start the JavaFX application thread
        launch(args);
    }
}