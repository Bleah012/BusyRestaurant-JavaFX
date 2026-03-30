package com.busyrestaurant;

public class AppSettings {
    // This would be changed depending on which tablet you are installing on
    private static String tableNumber = "T-01";

    public static String getTableNumber() {
        return tableNumber;
    }

    public static void setTableNumber(String newNumber) {
        tableNumber = newNumber;
    }
}