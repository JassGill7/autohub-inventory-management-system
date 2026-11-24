
package com.autohub.model;

public enum CarStatus {

    AVAILABLE("Available"),
    SOLD("Sold");

    private final String databaseValue;

    CarStatus(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public String getDatabaseValue() {
        return databaseValue;
    }

    public static CarStatus fromDatabaseValue(String value) {

        for (CarStatus status : CarStatus.values()) {

            if (status.databaseValue.equalsIgnoreCase(value)) {
                return status;
            }
        }

        throw new IllegalArgumentException(
                "Invalid car status: " + value);
    }
}
