
package com.autohub.dao;

import com.autohub.model.Car;
import com.autohub.model.CarSearchCriteria;

import java.util.List;
import java.util.Optional;

public interface CarDao {

    // Register a new car and return its generated database ID
    long insert(Car car);

    // Find a car using its database ID
    Optional<Car> findById(long carId);

    // Search cars using multiple search criteria
    List<Car> search(CarSearchCriteria criteria);

    // Update an existing car
    // Returns the number of rows affected
    int update(Car car);

    // Retrieve all cars from the database
    List<Car> findAll();

    Optional<Car> findByStockCode(String stockCode);

}
