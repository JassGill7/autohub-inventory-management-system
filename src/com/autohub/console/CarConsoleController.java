
package com.autohub.console;

import com.autohub.model.Car;
import com.autohub.model.CarStatus;
import com.autohub.service.CarService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CarConsoleController {

    private final InputReader inputReader;
    private final ConsoleMenu consoleMenu;
    private final CarService carService;

    private boolean running = true;

    public CarConsoleController(
            InputReader inputReader,
            ConsoleMenu consoleMenu,
            CarService carService) {

        if (inputReader == null
                || consoleMenu == null
                || carService == null) {
            throw new IllegalArgumentException(
                    "Controller dependencies cannot be null.");
        }

        this.inputReader = inputReader;
        this.consoleMenu = consoleMenu;
        this.carService = carService;
    }

    // Main application loop
    public void start() {

        while (running) {

            int choice = consoleMenu.showMenu();

            switch (choice) {

                case 1:
                    registerCar();
                    break;

                case 2:
                    viewAllCars();
                    break;

                case 3:
                    searchCarById();
                    break;

                case 4:
                    searchCarByStockCode();
                    break;

                case 5:
                    running = false;
                    consoleMenu.displayExitMessage();
                    break;

                default:
                    System.out.println("Invalid menu choice.");
            }
        }
    }

    // Register a new car
    private void registerCar() {

        System.out.println("\n--- Register New Car ---");

        try {
            Car car = new Car();

            car.setStockCode(inputReader.readRequiredLine("Enter stock code: "));

            car.setName(
                    inputReader.readRequiredLine("Enter car name: "));

            car.setMake(
                    inputReader.readRequiredLine("Enter car make: "));

            car.setModel(
                    inputReader.readRequiredLine("Enter car model: "));

            car.setManufactureYear(
                    inputReader.readInt("Enter manufacture year: "));

            car.setFuelType(
                    inputReader.readRequiredLine(
                            "Enter fuel type (P/D/E): ").toUpperCase());

            car.setTransmission(
                    inputReader.readRequiredLine(
                            "Enter transmission (M/A): ").toUpperCase());

            car.setColour(
                    inputReader.readRequiredLine("Enter colour: "));

            BigDecimal purchasePrice = inputReader.readBigDecimal(
                    "Enter purchase price: ");

            car.setPurchasePrice(purchasePrice);

            car.setAcquisitionDate(
                    inputReader.readDate(
                            "Enter acquisition date (YYYY-MM-DD): "));

            BigDecimal listingPrice = inputReader.readBigDecimal(
                    "Enter listing price: ");

            car.setListingPrice(listingPrice);

            car.setStatus(CarStatus.AVAILABLE);

            car.setNotes(
                    inputReader.readLine("Enter notes (optional): "));

            long generatedId = carService.registerCar(car);

            System.out.println("\nCar registered successfully.");
            System.out.println("Generated Car ID: " + generatedId);

        } catch (RuntimeException e) {

            System.out.println("\nCar registration failed.");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    // Display all cars
    private void viewAllCars() {

        System.out.println("\n--- Inventory ---");

        try {
            List<Car> cars = carService.findAllCars();

            if (cars == null || cars.isEmpty()) {
                System.out.println("No cars found in inventory.");
                return;
            }

            System.out.println("Total cars: " + cars.size());

            for (Car car : cars) {
                System.out.println("----------------------------------------");
                System.out.println(car);
            }

            System.out.println("----------------------------------------");

        } catch (RuntimeException e) {

            System.out.println("Unable to retrieve inventory.");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    // Search car by database ID
    private void searchCarById() {

        System.out.println("\n--- Search Car by ID ---");

        try {
            long carId = inputReader.readLong("Enter car ID: ");

            Optional<Car> result = carService.findCarById(carId);

            if (result.isPresent()) {
                System.out.println("\nCar found:");
                System.out.println(result.get());
            } else {
                System.out.println("No car found with ID: " + carId);
            }

        } catch (RuntimeException e) {

            System.out.println("Car search failed.");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    // Search car by stock code
    private void searchCarByStockCode() {

        System.out.println("\n--- Search Car by Stock Code ---");

        try {
            String stockCode = inputReader.readRequiredLine(
                    "Enter stock code: ");

            Optional<Car> result = carService.findCarByStockCode(stockCode);

            if (result.isPresent()) {
                System.out.println("\nCar found:");
                System.out.println(result.get());
            } else {
                System.out.println(
                        "No car found with stock code: " + stockCode);
            }

        } catch (RuntimeException e) {

            System.out.println("Car search failed.");
            System.out.println("Reason: " + e.getMessage());
        }
    }
}
