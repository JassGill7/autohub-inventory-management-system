
package com.autohub.model;

import java.math.BigDecimal;

public class CarSearchCriteria {

    // Search filters
    private String stockCode;
    private String make;
    private String model;
    private Integer manufactureYear;
    private CarStatus status;

    // Listing price range
    private BigDecimal minListingPrice;
    private BigDecimal maxListingPrice;


    // No-argument constructor
    public CarSearchCriteria() {
    }


    // Getters and Setters

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
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

    public Integer getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(Integer manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public CarStatus getStatus() {
        return status;
    }

    public void setStatus(CarStatus status) {
        this.status = status;
    }

    public BigDecimal getMinListingPrice() {
        return minListingPrice;
    }

    public void setMinListingPrice(BigDecimal minListingPrice) {
        this.minListingPrice = minListingPrice;
    }

    public BigDecimal getMaxListingPrice() {
        return maxListingPrice;
    }

    public void setMaxListingPrice(BigDecimal maxListingPrice) {
        this.maxListingPrice = maxListingPrice;
    }


    // String representation
    @Override
    public String toString() {

        return "CarSearchCriteria {" +
                "stockCode='" + stockCode + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", manufactureYear=" + manufactureYear +
                ", status=" + status +
                ", minListingPrice=" + minListingPrice +
                ", maxListingPrice=" + maxListingPrice +
                '}';
    }
}
