
package com.autohub.test;

import com.autohub.dao.CarDao;
import com.autohub.dao.JdbcCarDao;

import com.autohub.model.Car;
import com.autohub.model.CarStatus;

import com.autohub.service.CarService;
import com.autohub.validation.CarValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class CarRegistrationTest {

    public static void main(String[] args) {

        // Create dependencies
        CarDao carDao = new JdbcCarDao();

        CarValidator validator = new CarValidator();

        CarService carService =
                new CarService(carDao, validator);


        // Create a new car object
        Car car = new Car();

        // Generate a unique stock code for repeated testing
        String stockCode = "AH-TEST-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();

        car.setStockCode(stockCode);

        car.setName("Toyota Fortuner Legender");

        car.setMake("Toyota");

        car.setModel("Fortuner");

        car.setManufactureYear(2023);

        car.setFuelType("D");

        car.setTransmission("A");

        car.setColour("Black");

        car.setPurchasePrice(
                new BigDecimal("3200000.00")
        );

        car.setAcquisitionDate(
                LocalDate.now().minusDays(10)
        );

        car.setListingPrice(
                new BigDecimal("3500000.00")
        );

        car.setStatus(CarStatus.AVAILABLE);

        car.setNotes("AutoHub registration test");


        try {

            System.out.println(
                    "========== CAR REGISTRATION TEST =========="
            );

            // Register car
            long generatedId =
                    carService.registerCar(car);

            System.out.println(
                    "\nCar registered successfully!"
            );

            System.out.println(
                    "Generated Car ID: " + generatedId
            );

            System.out.println(
                    "Stock Code: " + stockCode
            );

            // Verify ID was assigned to original object
            System.out.println(
                    "Object Car ID: " + car.getCarId()
            );


            // Retrieve inserted car from MySQL
            Optional<Car> savedCar =
                    carDao.findById(generatedId);

            if (savedCar.isEmpty()) {

                throw new AssertionError(
                        "Registered car could not be retrieved."
                );
            }

            Car retrievedCar = savedCar.get();


            // Verify retrieved information
            if (!stockCode.equals(retrievedCar.getStockCode())) {

                throw new AssertionError(
                        "Stock code verification failed."
                );
            }

            if (!"Toyota".equals(retrievedCar.getMake())) {

                throw new AssertionError(
                        "Make verification failed."
                );
            }

            if (!new BigDecimal("3500000.00")
                    .equals(retrievedCar.getListingPrice())) {

                throw new AssertionError(
                        "Listing price verification failed."
                );
            }


            System.out.println(
                    "\nDatabase retrieval successful!"
            );

            System.out.println(
                    "\nRetrieved Vehicle Details:"
            );

            System.out.println(retrievedCar);


            System.out.println(
                    "\nREGISTRATION TEST PASSED!"
            );

        } catch (Exception e) {

            System.out.println(
                    "\nREGISTRATION TEST FAILED!"
            );

            e.printStackTrace();

            System.exit(1);
        }
    }
}
