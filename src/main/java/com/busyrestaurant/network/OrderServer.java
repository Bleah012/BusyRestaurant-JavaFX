package com.busyrestaurant.network;

import com.busyrestaurant.model.Order;
import com.google.gson.Gson;
import javafx.application.Platform;
import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class OrderServer {
    private static final int PORT = 12345;
    private static Gson gson = new Gson();

    // We pass a Consumer<Order> so the UI can define what to do with the order
    public static void startServer(Consumer<Order> onOrderReceived) {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Kitchen Server started on port " + PORT);
                while (true) {
                    try (Socket clientSocket = serverSocket.accept();
                         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                        String jsonOrder = in.readLine();
                        if (jsonOrder != null) {
                            Order receivedOrder = gson.fromJson(jsonOrder, Order.class);

                            // Log to console for debugging
                            System.out.println("Order Received: Table " + receivedOrder.getTableNumber());

                            // CRITICAL: Push the order to the UI thread
                            Platform.runLater(() -> onOrderReceived.accept(receivedOrder));
                        }
                    } catch (IOException e) {
                        System.err.println("Error handling client: " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                System.err.println("Could not start server: " + e.getMessage());
            }
        }).start();
    }
}