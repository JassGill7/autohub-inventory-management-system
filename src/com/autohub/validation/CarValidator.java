
package com.autohub.validation;

import com.autohub.model.Car;
import com.autohub.model.CarStatus;
import com.autohub.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class CarValidator {

    // VIN must contain exactly 17 valid characters.
    // I, O and Q are excluded.
    private static final Pattern VIN_PATTERN =
            Pattern.compile("^[A-HJ-NPR-Z0-9]{17}$");


    // Main validation method
    public void validate(Car car) {

        if (car == null) {
            throw new ValidationException(
                    List.of("Car object cannot be null.")
            );
        }

        List<String> errors = new ArrayList<>();


        // 1. Required field validation

        validateRequiredText(
                errors, "Stock code", car.getStockCode(), 30
        );

        validateRequiredText(
                errors, "Name", car.getName(), 100
        );

        validateRequiredText(
                errors, "Make", car.getMake(), 60
        );

        validateRequiredText(
                errors, "Model", car.getModel(), 60
        );


        // 2. Manufacture year validation

        int currentYear = Year.now().getValue();

        if (car.getManufactureYear() < 1901 ||
                car.getManufactureYear() > currentYear) {

            errors.add(
                    "Manufacture year must be between 1901 and "
                            + currentYear + "."
            );
        }


        // 3. Optional field validation

        validateVIN(errors, car.getVin());

        validateOptionalLength(
                errors, "Registration number",
                car.getRegistrationNumber(), 20
        );

        validateOptionalLength(
                errors, "Colour", car.getColour(), 30
        );


        // 4. Fuel type validation

        String fuelType = car.getFuelType();

        if (fuelType == null || fuelType.isBlank()) {

            errors.add("Fuel type is required.");

        } else {

            fuelType = fuelType.trim().toUpperCase(Locale.ROOT);

            if (!fuelType.equals("P")
                    && !fuelType.equals("D")
                    && !fuelType.equals("E")) {

                errors.add(
                        "Fuel type must be P, D or E."
                );
            }
        }


        // 5. Transmission validation

        String transmission = car.getTransmission();

        if (transmission == null || transmission.isBlank()) {

            errors.add("Transmission is required.");

        } else {

            transmission = transmission.trim().toUpperCase(Locale.ROOT);

            if (!transmission.equals("M")
                    && !transmission.equals("A")) {

                errors.add(
                        "Transmission must be M or A."
                );
            }
        }


        // 6. Financial validation

        validatePrice(
                errors, "Purchase price", car.getPurchasePrice(), true
        );

        validatePrice(
                errors, "Listing price", car.getListingPrice(), true
        );

        validatePrice(
                errors, "Sold price", car.getSoldPrice(), false
        );


        // 7. Acquisition date validation

        LocalDate acquisitionDate = car.getAcquisitionDate();

        if (acquisitionDate == null) {

            errors.add("Acquisition date is required.");

        } else if (acquisitionDate.isAfter(LocalDate.now())) {

            errors.add("Acquisition date cannot be in the future.");
        }


        // 8. Status validation

        CarStatus status = car.getStatus();

        if (status == null) {

            errors.add("Car status is required.");

        } else {

            validateSaleDetails(errors, car);
        }


        // Throw all collected validation errors together

        if (!errors.isEmpty()) {

            throw new ValidationException(errors);
        }
    }


    // Check required text fields

    private void validateRequiredText(
            List<String> errors,
            String fieldName,
            String value,
            int maxLength
    ) {

        if (value == null || value.isBlank()) {

            errors.add(fieldName + " is required.");

        } else if (value.trim().length() > maxLength) {

            errors.add(
                    fieldName + " cannot exceed "
                            + maxLength + " characters."
            );
        }
    }


    // Validate optional text fields

    private void validateOptionalLength(
            List<String> errors,
            String fieldName,
            String value,
            int maxLength
    ) {

        if (value != null
                && !value.isBlank()
                && value.trim().length() > maxLength) {

            errors.add(
                    fieldName + " cannot exceed "
                            + maxLength + " characters."
            );
        }
    }


    // VIN validation

    private void validateVIN(
            List<String> errors,
            String vin
    ) {

        // VIN is optional.
        if (vin == null || vin.isBlank()) {
            return;
        }

        String normalizedVIN = vin.trim().toUpperCase(Locale.ROOT);

        if (!VIN_PATTERN.matcher(normalizedVIN).matches()) {

            errors.add(
                    "VIN must contain exactly 17 valid characters "
                            + "and cannot contain I, O or Q."
            );
        }
    }


    // Price validation

    private void validatePrice(
            List<String> errors,
            String fieldName,
            BigDecimal price,
            boolean required
    ) {

        if (price == null) {

            if (required) {
                errors.add(fieldName + " is required.");
            }

            return;
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {

            errors.add(fieldName + " cannot be negative.");
        }
    }


    // Sale information validation

    private void validateSaleDetails(
            List<String> errors,
            Car car
    ) {

        CarStatus status = car.getStatus();

        LocalDate soldDate = car.getSoldDate();
        BigDecimal soldPrice = car.getSoldPrice();

        LocalDate acquisitionDate = car.getAcquisitionDate();


        if (status == CarStatus.AVAILABLE) {

            if (soldDate != null) {

                errors.add(
                        "Available cars cannot have a sold date."
                );
            }

            if (soldPrice != null) {

                errors.add(
                        "Available cars cannot have a sold price."
                );
            }
        }


        if (status == CarStatus.SOLD) {

            if (soldDate == null) {

                errors.add(
                        "Sold date is required for sold cars."
                );

            } else {

                if (soldDate.isAfter(LocalDate.now())) {

                    errors.add(
                            "Sold date cannot be in the future."
                    );
                }

                if (acquisitionDate != null
                        && soldDate.isBefore(acquisitionDate)) {

                    errors.add(
                            "Sold date cannot precede acquisition date."
                    );
                }
            }

            if (soldPrice == null) {

                errors.add(
                        "Sold price is required for sold cars."
                );
            }
        }
    }
}
