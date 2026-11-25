
package com.autohub.console;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputReader implements AutoCloseable {

    private final Scanner scanner;

    // Initialize Scanner
    public InputReader() {
        this(new Scanner(System.in));
    }

    // Constructor useful for testing with custom input
    public InputReader(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner cannot be null.");
        }

        this.scanner = scanner;
    }

    // Read a string
    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // Read a non-empty string
    public String readRequiredLine(String prompt) {

        while (true) {
            String input = readLine(prompt);

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("Input cannot be empty. Try again.");
        }
    }

    // Read an integer
    public int readInt(String prompt) {

        while (true) {
            String input = readRequiredLine(prompt);

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter an integer.");
            }
        }
    }

    // Read an integer within a specified range
    public int readIntInRange(String prompt, int min, int max) {

        while (true) {
            int value = readInt(prompt);

            if (value >= min && value <= max) {
                return value;
            }

            System.out.println(
                    "Enter a value between " + min + " and " + max + ".");
        }
    }

    // Read a long integer
    public long readLong(String prompt) {

        while (true) {
            String input = readRequiredLine(prompt);

            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a valid number.");
            }
        }
    }

    // Read a decimal value
    public BigDecimal readBigDecimal(String prompt) {

        while (true) {
            String input = readRequiredLine(prompt);

            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a valid decimal.");
            }
        }
    }

    // Read a date in YYYY-MM-DD format
    public LocalDate readDate(String prompt) {

        while (true) {
            String input = readRequiredLine(prompt);

            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println(
                        "Invalid date. Use YYYY-MM-DD format.");
            }
        }
    }

    // Close Scanner when the application terminates
    @Override
    public void close() {
        scanner.close();
    }
}
