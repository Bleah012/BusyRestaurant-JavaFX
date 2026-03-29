package com.busyrestaurant.network; // Add this line!

import com.busyrestaurant.model.Order;
import com.google.gson.Gson;
import java.io.*;
import java.net.*;

public class OrderClient {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;
    private static Gson gson = new Gson();

    public static void sendOrder(Order order) {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String jsonOrder = gson.toJson(order);
            out.println(jsonOrder);
            System.out.println("Order successfully pushed to Kitchen Server.");

        } catch (IOException e) {
            System.err.println("Connection Failed: Ensure the Kitchen Server is running.");
        }
    }
}