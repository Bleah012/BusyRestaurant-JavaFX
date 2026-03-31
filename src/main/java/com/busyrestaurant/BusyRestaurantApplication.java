package com.busyrestaurant;

import com.busyrestaurant.util.DatabaseManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main Entry Point for the BusyRestaurant Self-Ordering System.
 * This class initializes the JavaFX Primary Stage and Database Connection.
 */
public class BusyRestaurantApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Initialize and Load the Menu from Database
            MenuManager.getInstance().loadFromDatabase();

            System.out.println("DATABASE READY & MENU LOADED");
        } catch (Exception e) {
            System.err.println("INITIALIZATION ERROR: " + e.getMessage());
        }

        // 2. Launch the UI
        WelcomeView.show(primaryStage);
        //KitchenView.show(primaryStage);
        //AdminView.show(primaryStage);
        //MenuView.show(primaryStage);
    }
    /**
     * Cleanly shut down the database factory when the app is closed.
     */
    @Override
    public void stop() {
        DatabaseManager.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}