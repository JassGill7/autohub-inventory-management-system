package com.autohub;

import com.autohub.console.CarConsoleController;
import com.autohub.console.ConsoleMenu;
import com.autohub.console.InputReader;

import com.autohub.dao.CarDao;
import com.autohub.dao.JdbcCarDao;

import com.autohub.service.CarService;
import com.autohub.validation.CarValidator;

/**
 * Application entry point for the AutoHub Inventory Management System.
 *
 * This class is the composition root. It creates the dependencies
 * and connects the DAO, service, and presentation layers.
 */
public final class AutoHubApplication {

    private AutoHubApplication() {
        // Prevent instantiation.
    }

    public static void main(String[] args) {

        // 1. Data access layer
        CarDao carDao = new JdbcCarDao();

        // 2. Validation layer
        CarValidator carValidator = new CarValidator();

        // 3. Business/service layer
        CarService carService = new CarService(carDao, carValidator);

        // 4. Presentation layer
        try (InputReader inputReader = new InputReader()) {

            ConsoleMenu consoleMenu = new ConsoleMenu(inputReader);

            CarConsoleController controller = new CarConsoleController(
                    inputReader,
                    consoleMenu,
                    carService);

            // 5. Start the application
            controller.start();
        }
    }
}