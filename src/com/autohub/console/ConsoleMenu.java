
package com.autohub.console;

public class ConsoleMenu {

    private final InputReader inputReader;

    private static final int MIN_CHOICE = 1;
    private static final int MAX_CHOICE = 5;

    // Constructor
    public ConsoleMenu(InputReader inputReader) {

        if (inputReader == null) {
            throw new IllegalArgumentException(
                    "InputReader cannot be null.");
        }

        this.inputReader = inputReader;
    }

    // Display the main menu and return user's choice
    public int showMenu() {

        System.out.println();
        System.out.println("========================================");
        System.out.println("       AUTOHUB INVENTORY SYSTEM");
        System.out.println("          MANAGEMENT SYSTEM");
        System.out.println("========================================");

        System.out.println("1. Register New Car");
        System.out.println("2. View All Cars");
        System.out.println("3. Search Car by ID");
        System.out.println("4. Search Car by Stock Code");
        System.out.println("5. Exit");

        System.out.println("========================================");

        return inputReader.readIntInRange(
                "Enter your choice: ",
                MIN_CHOICE,
                MAX_CHOICE);
    }

    // Display exit message
    public void displayExitMessage() {

        System.out.println();
        System.out.println("========================================");
        System.out.println("  Thank you for using AutoHub!");
        System.out.println("========================================");
    }
}
