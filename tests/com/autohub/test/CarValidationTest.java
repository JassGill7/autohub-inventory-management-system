
package com.autohub.test;

import com.autohub.model.Car;
import com.autohub.model.CarStatus;
import com.autohub.model.CarSearchCriteria;

import com.autohub.validation.CarValidator;
import com.autohub.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;

public class CarValidationTest {

    private static final CarValidator validator = new CarValidator();

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("     AUTOHUB MODEL & VALIDATION TESTS");
        System.out.println("======================================\n");

        // 1. Object creation tests

        runTest("Default Car status",
                CarValidationTest::testDefaultStatus);

        runTest("Car attributes and getters/setters",
                CarValidationTest::testCarAttributes);

        runTest("CarSearchCriteria creation",
                CarValidationTest::testSearchCriteria);

        // 2. Valid vehicle tests

        runTest("Valid available vehicle",
                CarValidationTest::testValidAvailableCar);

        runTest("Valid sold vehicle",
                CarValidationTest::testValidSoldCar);

        runTest("Optional fields can be NULL",
                CarValidationTest::testOptionalFields);

        // 3. Invalid vehicle tests

        runTest("NULL Car object",
                CarValidationTest::testNullCar);

        runTest("Missing required fields",
                CarValidationTest::testRequiredFields);

        runTest("Invalid manufacture year",
                CarValidationTest::testInvalidYear);

        runTest("Invalid VIN",
                CarValidationTest::testInvalidVIN);

        runTest("Invalid fuel and transmission",
                CarValidationTest::testInvalidSpecifications);

        runTest("Negative prices",
                CarValidationTest::testNegativePrices);

        // 4. Date and status validation

        runTest("Future acquisition date",
                CarValidationTest::testFutureAcquisitionDate);

        runTest("Sold date before acquisition date",
                CarValidationTest::testSoldDateBeforeAcquisition);

        runTest("Available car with sale details",
                CarValidationTest::testAvailableCarSaleDetails);

        runTest("Sold car without sale details",
                CarValidationTest::testSoldCarMissingDetails);

        runTest("Future sold date",
                CarValidationTest::testFutureSoldDate);

        // Final summary

        System.out.println("\n======================================");
        System.out.println("              TEST SUMMARY");
        System.out.println("======================================");

        System.out.println("Total Tests : " + (passed + failed));
        System.out.println("Passed      : " + passed);
        System.out.println("Failed      : " + failed);

        System.out.println("======================================");

        if (failed == 0) {

            System.out.println("ALL TESTS PASSED!");

        } else {

            System.out.println("SOME TESTS FAILED!");
            System.exit(1);
        }
    }

    // Test execution method

    private static void runTest(
            String testName,
            Runnable test) {

        try {

            test.run();

            passed++;

            System.out.println("[PASS] " + testName);

        } catch (Throwable error) {

            failed++;

            System.out.println("[FAIL] " + testName);

            System.out.println(
                    "       Reason: " + error.getMessage());
        }
    }

    // Helper method to create a valid available car

    private static Car createValidCar() {

        Car car = new Car();

        car.setStockCode("AH-101");

        car.setName("Toyota Fortuner Legender");

        car.setMake("Toyota");

        car.setModel("Fortuner");

        car.setManufactureYear(2023);

        car.setVin("1HGCM82633A004352");

        car.setRegistrationNumber("PB10AB1234");

        car.setFuelType("D");

        car.setTransmission("A");

        car.setColour("Black");

        car.setPurchasePrice(
                new BigDecimal("3200000.00"));

        car.setAcquisitionDate(
                LocalDate.now().minusDays(30));

        car.setListingPrice(
                new BigDecimal("3500000.00"));

        car.setStatus(CarStatus.AVAILABLE);

        car.setSoldDate(null);

        car.setSoldPrice(null);

        car.setNotes("Test vehicle");

        return car;
    }

    // Helper method to verify validation failure

    private static ValidationException expectValidationFailure(
            Car car) {

        try {

            validator.validate(car);

        } catch (ValidationException e) {

            return e;
        }

        throw new AssertionError(
                "Expected ValidationException, but validation passed.");
    }

    // 1. Default status test

    private static void testDefaultStatus() {

        Car car = new Car();

        assertEquals(
                CarStatus.AVAILABLE,
                car.getStatus(),
                "Default status should be AVAILABLE.");
    }

    // 2. Car attributes test

    private static void testCarAttributes() {

        Car car = createValidCar();

        assertEquals(
                "AH-101",
                car.getStockCode(),
                "Stock code mismatch.");

        assertEquals(
                "Toyota",
                car.getMake(),
                "Make mismatch.");

        assertEquals(
                "Fortuner",
                car.getModel(),
                "Model mismatch.");

        assertEquals(
                new BigDecimal("3200000.00"),
                car.getPurchasePrice(),
                "Purchase price mismatch.");

        assertEquals(
                LocalDate.now().minusDays(30),
                car.getAcquisitionDate(),
                "Acquisition date mismatch.");

        assertTrue(
                car.toString().contains("AH-101"),
                "toString() should contain stock code.");
    }

    // 3. Search criteria test

    private static void testSearchCriteria() {

        CarSearchCriteria criteria = new CarSearchCriteria();

        criteria.setMake("Toyota");

        criteria.setModel("Fortuner");

        criteria.setStatus(CarStatus.AVAILABLE);

        criteria.setMinListingPrice(
                new BigDecimal("2000000"));

        criteria.setMaxListingPrice(
                new BigDecimal("4000000"));

        assertEquals(
                "Toyota",
                criteria.getMake(),
                "Make filter mismatch.");

        assertEquals(
                "Fortuner",
                criteria.getModel(),
                "Model filter mismatch.");

        assertEquals(
                CarStatus.AVAILABLE,
                criteria.getStatus(),
                "Status filter mismatch.");

        assertEquals(
                new BigDecimal("4000000"),
                criteria.getMaxListingPrice(),
                "Maximum price mismatch.");
    }

    // 4. Valid available car

    private static void testValidAvailableCar() {

        Car car = createValidCar();

        validator.validate(car);
    }

    // 5. Valid sold car

    private static void testValidSoldCar() {

        Car car = createValidCar();

        car.setStatus(CarStatus.SOLD);

        car.setSoldDate(LocalDate.now().minusDays(2));

        car.setSoldPrice(
                new BigDecimal("3300000.00"));

        validator.validate(car);
    }

    // 6. Optional fields test

    private static void testOptionalFields() {

        Car car = createValidCar();

        car.setVin(null);

        car.setRegistrationNumber(null);

        car.setColour(null);

        car.setNotes(null);

        validator.validate(car);
    }

    // 7. NULL Car test

    private static void testNullCar() {

        ValidationException exception = expectValidationFailure(null);

        assertContains(
                exception.getErrors(),
                "cannot be null");
    }

    // 8. Required fields test

    private static void testRequiredFields() {

        Car car = createValidCar();

        car.setStockCode(" ");

        car.setName(null);

        car.setMake("");

        car.setModel(" ");

        ValidationException exception = expectValidationFailure(car);

        List<String> errors = exception.getErrors();

        assertContains(errors, "Stock code");

        assertContains(errors, "Name");

        assertContains(errors, "Make");

        assertContains(errors, "Model");

        assertTrue(
                errors.size() >= 4,
                "Expected multiple validation errors.");
    }

    // 9. Manufacture year test

    private static void testInvalidYear() {

        Car car = createValidCar();

        car.setManufactureYear(1800);

        ValidationException exception = expectValidationFailure(car);

        assertContains(
                exception.getErrors(),
                "Manufacture year");
    }

    // 10. VIN test

    private static void testInvalidVIN() {

        Car car = createValidCar();

        car.setVin("INVALIDVIN");

        ValidationException exception = expectValidationFailure(car);

        assertContains(
                exception.getErrors(),
                "VIN");
    }

    // 11. Fuel and transmission test

    private static void testInvalidSpecifications() {

        Car car = createValidCar();

        car.setFuelType("G");

        car.setTransmission("CVT");

        ValidationException exception = expectValidationFailure(car);

        assertContains(
                exception.getErrors(),
                "Fuel type");

        assertContains(
                exception.getErrors(),
                "Transmission");
    }

    // 12. Negative price test

    private static void testNegativePrices() {

        Car car = createValidCar();

        car.setPurchasePrice(
                new BigDecimal("-1000"));

        car.setListingPrice(
                new BigDecimal("-2000"));

        ValidationException exception = expectValidationFailure(car);

        assertContains(
                exception.getErrors(),
                "Purchase price");

        assertContains(
                exception.getErrors(),
                "Listing price");
    }

    // 13. Future acquisition date

    private static void testFutureAcquisitionDate() {

        Car car = createValidCar();

        car.setAcquisitionDate(
                LocalDate.now().plusDays(1));

        ValidationException exception = expectValidationFailure(car);

        assertContains(
                exception.getErrors(),
                "Acquisition date");
    }

    // 14. Sold date before acquisition date

    private static void testSoldDateBeforeAcquisition() {

        Car car = createValidCar();

        car.setStatus(CarStatus.SOLD);

        car.setAcquisitionDate(
                LocalDate.now().minusDays(10));

        car.setSoldDate(
                LocalDate.now().minusDays(20));

        car.setSoldPrice(
                new BigDecimal("3000000"));

        ValidationException exception = expectValidationFailure(car);

        assertContains(
                exception.getErrors(),
                "precede acquisition date");
    }

    // 15. Available car cannot have sale details

    private static void testAvailableCarSaleDetails() {

        Car car = createValidCar();

        car.setStatus(CarStatus.AVAILABLE);

        car.setSoldDate(LocalDate.now().minusDays(1));

        car.setSoldPrice(
                new BigDecimal("3000000"));

        ValidationException exception = expectValidationFailure(car);

        assertContains(
                exception.getErrors(),
                "Available cars cannot have a sold date");
    }

    // 16. Sold car must have sale details

    private static void testSoldCarMissingDetails() {

        Car car = createValidCar();

        car.setStatus(CarStatus.SOLD);

        car.setSoldDate(null);

        car.setSoldPrice(null);

        ValidationException exception = expectValidationFailure(car);

        assertContains(
                exception.getErrors(),
                "Sold date is required");

        assertContains(
                exception.getErrors(),
                "Sold price is required");
    }

    // 17. Future sold date

    private static void testFutureSoldDate() {

        Car car = createValidCar();

        car.setStatus(CarStatus.SOLD);

        car.setSoldDate(
                LocalDate.now().plusDays(5));

        car.setSoldPrice(
                new BigDecimal("3300000"));

        ValidationException exception = expectValidationFailure(car);

        assertContains(
                exception.getErrors(),
                "Sold date cannot be in the future");
    }

    // Assertion helpers

    private static void assertEquals(
            Object expected,
            Object actual,
            String message) {

        if (expected == null
                ? actual != null
                : !expected.equals(actual)) {

            throw new AssertionError(
                    message + " Expected: " + expected
                            + ", Actual: " + actual);
        }
    }

    private static void assertTrue(
            boolean condition,
            String message) {

        if (!condition) {

            throw new AssertionError(message);
        }
    }

    private static void assertContains(
            List<String> errors,
            String expectedText) {

        boolean found = errors.stream()
                .anyMatch(error -> error.toLowerCase()
                        .contains(expectedText.toLowerCase()));

        if (!found) {

            throw new AssertionError(
                    "Expected error containing: " + expectedText
                            + ". Actual errors: " + errors);
        }
    }
}
