
package com.autohub.service;

import com.autohub.dao.CarDao;
import com.autohub.model.Car;
import com.autohub.validation.CarValidator;

import java.util.Objects;
import java.util.Optional;

public class CarService {

    private final CarDao carDao;
    private final CarValidator validator;

    // Constructor-based dependency injection
    public CarService(CarDao carDao, CarValidator validator) {

        this.carDao = Objects.requireNonNull(
                carDao,
                "CarDao cannot be null.");

        this.validator = Objects.requireNonNull(
                validator,
                "CarValidator cannot be null.");
    }

    // Register a new car
    public long registerCar(Car car) {

        // A new car must not already have a database ID
        if (car != null && car.getCarId() != null) {

            throw new IllegalArgumentException(
                    "Cannot register an existing car. "
                            + "A new car must not have a database ID.");
        }

        // Validate vehicle information
        validator.validate(car);

        // Insert the car into MySQL
        return carDao.insert(car);
    }

    public Optional<Car> findCarById(long carId) {

        if (carId <= 0) {
            throw new IllegalArgumentException(
                    "Car ID must be greater than zero.");
        }

        return carDao.findById(carId);
    }

    public Optional<Car> findCarByStockCode(String stockCode) {

        if (stockCode == null || stockCode.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Stock code cannot be empty.");
        }

        return carDao.findByStockCode(stockCode.trim());
    }

}
