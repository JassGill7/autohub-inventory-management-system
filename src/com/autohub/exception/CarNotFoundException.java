
package com.autohub.exception;

public class CarNotFoundException extends RuntimeException {

    // Constructor for a custom error message
    public CarNotFoundException(String message) {
        super(message);
    }

    // Constructor for searching by database ID
    public CarNotFoundException(long carId) {
        super("No car found with ID: " + carId);
    }

    // Constructor for searching by another identifier
    public CarNotFoundException(
            String identifierType,
            String identifierValue) {
        super("No car found with " + identifierType
                + ": " + identifierValue);
    }
}
