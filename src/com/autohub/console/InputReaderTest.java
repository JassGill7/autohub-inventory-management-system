package com.autohub.console;

public class InputReaderTest {

    public static void main(String[] args) {

        try (InputReader input = new InputReader()) {

            String name = input.readRequiredLine("Enter car name: ");

            int year = input.readInt("Enter manufacture year: ");

            BigDecimalTest(input);

            System.out.println("\nEntered Details:");
            System.out.println("Car Name: " + name);
            System.out.println("Manufacture Year: " + year);
        }
    }

    private static void BigDecimalTest(InputReader input) {
        java.math.BigDecimal price =
            input.readBigDecimal("Enter purchase price: ");

        System.out.println("Purchase Price: " + price);
    }
}