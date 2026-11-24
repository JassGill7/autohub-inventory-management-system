
package com.autohub.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Car {

    // Database identifier
    private Long carId;

    // Basic vehicle information
    private String stockCode;
    private String name;
    private String make;
    private String model;
    private int manufactureYear;

    // Vehicle identification
    private String vin;
    private String registrationNumber;

    // Vehicle specifications
    private String fuelType;
    private String transmission;
    private String colour;

    // Financial information
    private BigDecimal purchasePrice;
    private BigDecimal listingPrice;

    // Inventory information
    private LocalDate acquisitionDate;
    private CarStatus status;

    // Sale information
    private LocalDate soldDate;
    private BigDecimal soldPrice;

    // Additional information
    private String notes;

    // No-argument constructor
    public Car() {
        this.status = CarStatus.AVAILABLE;
    }

    // Getters and Setters

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(int manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getListingPrice() {
        return listingPrice;
    }

    public void setListingPrice(BigDecimal listingPrice) {
        this.listingPrice = listingPrice;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public CarStatus getStatus() {
        return status;
    }

    public void setStatus(CarStatus status) {
        this.status = status;
    }

    public LocalDate getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(LocalDate soldDate) {
        this.soldDate = soldDate;
    }

    public BigDecimal getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(BigDecimal soldPrice) {
        this.soldPrice = soldPrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // String representation of a Car object
    @Override
    public String toString() {

        return "Car {" +
                "carId=" + carId +
                ", stockCode='" + stockCode + '\'' +
                ", name='" + name + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", manufactureYear=" + manufactureYear +
                ", vin='" + vin + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", fuelType='" + fuelType + '\'' +
                ", transmission='" + transmission + '\'' +
                ", colour='" + colour + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", acquisitionDate=" + acquisitionDate +
                ", listingPrice=" + listingPrice +
                ", status=" + status +
                ", soldDate=" + soldDate +
                ", soldPrice=" + soldPrice +
                ", notes='" + notes + '\'' +
                '}';
    }
}
