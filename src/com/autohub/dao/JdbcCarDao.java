
package com.autohub.dao;

import com.autohub.db.ConnectionFactory;
import com.autohub.exception.DataAccessException;

import com.autohub.model.Car;
import com.autohub.model.CarStatus;
import com.autohub.model.CarSearchCriteria;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class JdbcCarDao implements CarDao {

        // Columns retrieved from the cars table
        private static final String SELECT_COLUMNS = """
                        SELECT car_id, stock_code, name, make, model,
                               manufacture_year, vin, registration_number,
                               fuel_type, transmission, colour,
                               purchase_price, acquisition_date, listing_price,
                               status, sold_date, sold_price, notes
                        FROM cars
                        """;

        // ==========================================
        // 1. INSERT - REGISTER A NEW CAR
        // ==========================================

        @Override
        public long insert(Car car) {

                if (car == null) {
                        throw new IllegalArgumentException(
                                        "Car object cannot be null.");
                }

                String sql = """
                                INSERT INTO cars (
                                    stock_code, name, make, model,
                                    manufacture_year, vin, registration_number,
                                    fuel_type, transmission, colour,
                                    purchase_price, acquisition_date, listing_price,
                                    status, sold_date, sold_price, notes
                                )
                                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
                                        ?, ?, ?, ?, ?, ?, ?)
                                """;

                try (Connection connection = ConnectionFactory.getConnection();

                                PreparedStatement statement = connection.prepareStatement(
                                                sql,
                                                Statement.RETURN_GENERATED_KEYS)) {

                        setCarParameters(statement, car, 1);

                        statement.executeUpdate();

                        // Retrieve automatically generated car_id
                        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                                if (generatedKeys.next()) {

                                        long generatedId = generatedKeys.getLong(1);

                                        car.setCarId(generatedId);

                                        return generatedId;
                                }
                        }

                        throw new SQLException(
                                        "Car insertion succeeded but no generated ID was returned.");

                } catch (SQLException e) {

                        throw new DataAccessException(
                                        "Failed to register car.",
                                        e);
                }
        }

        // ==========================================
        // 2. SELECT - FIND CAR BY ID
        // ==========================================

        @Override
        public Optional<Car> findById(long carId) {

                if (carId <= 0) {
                        throw new IllegalArgumentException(
                                        "Car ID must be positive.");
                }

                String sql = SELECT_COLUMNS + " WHERE car_id = ?";

                try (Connection connection = ConnectionFactory.getConnection();

                                PreparedStatement statement = connection.prepareStatement(sql)) {

                        statement.setLong(1, carId);

                        try (ResultSet resultSet = statement.executeQuery()) {

                                if (resultSet.next()) {

                                        return Optional.of(mapRow(resultSet));
                                }

                                return Optional.empty();
                        }

                } catch (SQLException e) {

                        throw new DataAccessException(
                                        "Failed to retrieve car with ID: " + carId,
                                        e);
                }
        }

        // ==========================================
        // 3. SELECT - SEARCH CARS USING CRITERIA
        // ==========================================

        @Override
        public List<Car> search(CarSearchCriteria criteria) {

                if (criteria == null) {
                        throw new IllegalArgumentException(
                                        "Search criteria cannot be null.");
                }

                BigDecimal minPrice = criteria.getMinListingPrice();
                BigDecimal maxPrice = criteria.getMaxListingPrice();

                if (minPrice != null && maxPrice != null
                                && minPrice.compareTo(maxPrice) > 0) {

                        throw new IllegalArgumentException(
                                        "Minimum listing price cannot exceed maximum listing price.");
                }

                StringBuilder sql = new StringBuilder(SELECT_COLUMNS);

                List<Object> parameters = new ArrayList<>();

                // Stock code - exact match
                if (hasText(criteria.getStockCode())) {

                        addCondition(
                                        sql,
                                        parameters,
                                        "stock_code = ?",
                                        criteria.getStockCode().trim());
                }

                // Make - partial match
                if (hasText(criteria.getMake())) {

                        addCondition(
                                        sql,
                                        parameters,
                                        "make LIKE ?",
                                        "%" + criteria.getMake().trim() + "%");
                }

                // Model - partial match
                if (hasText(criteria.getModel())) {

                        addCondition(
                                        sql,
                                        parameters,
                                        "model LIKE ?",
                                        "%" + criteria.getModel().trim() + "%");
                }

                // Manufacture year
                if (criteria.getManufactureYear() != null) {

                        addCondition(
                                        sql,
                                        parameters,
                                        "manufacture_year = ?",
                                        criteria.getManufactureYear());
                }

                // Car status
                if (criteria.getStatus() != null) {

                        addCondition(
                                        sql,
                                        parameters,
                                        "status = ?",
                                        criteria.getStatus().getDatabaseValue());
                }

                // Minimum listing price
                if (minPrice != null) {

                        addCondition(
                                        sql,
                                        parameters,
                                        "listing_price >= ?",
                                        minPrice);
                }

                // Maximum listing price
                if (maxPrice != null) {

                        addCondition(
                                        sql,
                                        parameters,
                                        "listing_price <= ?",
                                        maxPrice);
                }

                sql.append(" ORDER BY car_id ASC");

                try (Connection connection = ConnectionFactory.getConnection();

                                PreparedStatement statement = connection.prepareStatement(sql.toString())) {

                        // Bind search parameters
                        for (int i = 0; i < parameters.size(); i++) {

                                statement.setObject(
                                                i + 1,
                                                parameters.get(i));
                        }

                        try (ResultSet resultSet = statement.executeQuery()) {

                                List<Car> cars = new ArrayList<>();

                                while (resultSet.next()) {

                                        cars.add(mapRow(resultSet));
                                }

                                return cars;
                        }

                } catch (SQLException e) {

                        throw new DataAccessException(
                                        "Failed to search cars.",
                                        e);
                }
        }

        // ==========================================
        // 4. UPDATE - UPDATE EXISTING CAR
        // ==========================================

        @Override
        public int update(Car car) {

                if (car == null) {
                        throw new IllegalArgumentException(
                                        "Car object cannot be null.");
                }

                if (car.getCarId() == null || car.getCarId() <= 0) {

                        throw new IllegalArgumentException(
                                        "Valid Car ID is required for updating.");
                }

                String sql = """
                                UPDATE cars SET

                                    stock_code = ?,
                                    name = ?,
                                    make = ?,
                                    model = ?,
                                    manufacture_year = ?,

                                    vin = ?,
                                    registration_number = ?,

                                    fuel_type = ?,
                                    transmission = ?,
                                    colour = ?,

                                    purchase_price = ?,
                                    acquisition_date = ?,
                                    listing_price = ?,

                                    status = ?,
                                    sold_date = ?,
                                    sold_price = ?,

                                    notes = ?

                                WHERE car_id = ?
                                """;

                try (Connection connection = ConnectionFactory.getConnection();

                                PreparedStatement statement = connection.prepareStatement(sql)) {

                        int nextIndex = setCarParameters(statement, car, 1);

                        statement.setLong(
                                        nextIndex,
                                        car.getCarId());

                        return statement.executeUpdate();

                } catch (SQLException e) {

                        throw new DataAccessException(
                                        "Failed to update car with ID: "
                                                        + car.getCarId(),
                                        e);
                }
        }

        // ==========================================
        // 5. SELECT - RETRIEVE ALL CARS
        // ==========================================

        @Override
        public List<Car> findAll() {

                String sql = SELECT_COLUMNS + " ORDER BY car_id ASC";

                try (Connection connection = ConnectionFactory.getConnection();

                                PreparedStatement statement = connection.prepareStatement(sql);

                                ResultSet resultSet = statement.executeQuery()) {

                        List<Car> cars = new ArrayList<>();

                        while (resultSet.next()) {

                                cars.add(mapRow(resultSet));
                        }

                        return cars;

                } catch (SQLException e) {

                        throw new DataAccessException(
                                        "Failed to retrieve all cars.",
                                        e);
                }
        }

        // ==========================================
        // HELPER 1 - MAP RESULTSET TO CAR OBJECT
        // ==========================================

        private Car mapRow(ResultSet resultSet)
                        throws SQLException {

                Car car = new Car();

                car.setCarId(
                                resultSet.getLong("car_id"));

                car.setStockCode(
                                resultSet.getString("stock_code"));

                car.setName(
                                resultSet.getString("name"));

                car.setMake(
                                resultSet.getString("make"));

                car.setModel(
                                resultSet.getString("model"));

                car.setManufactureYear(
                                resultSet.getInt("manufacture_year"));

                // Vehicle identification

                car.setVin(
                                resultSet.getString("vin"));

                car.setRegistrationNumber(
                                resultSet.getString("registration_number"));

                // Vehicle specifications

                car.setFuelType(
                                resultSet.getString("fuel_type"));

                car.setTransmission(
                                resultSet.getString("transmission"));

                car.setColour(
                                resultSet.getString("colour"));

                // Financial information

                car.setPurchasePrice(
                                resultSet.getBigDecimal("purchase_price"));

                car.setListingPrice(
                                resultSet.getBigDecimal("listing_price"));

                // Date information

                car.setAcquisitionDate(
                                getLocalDate(resultSet, "acquisition_date"));

                // Status mapping

                car.setStatus(
                                CarStatus.fromDatabaseValue(
                                                resultSet.getString("status")));

                // Sale information

                car.setSoldDate(
                                getLocalDate(resultSet, "sold_date"));

                car.setSoldPrice(
                                resultSet.getBigDecimal("sold_price"));

                // Additional information

                car.setNotes(
                                resultSet.getString("notes"));

                return car;
        }

        // ==========================================
        // HELPER 2 - SET CAR PARAMETERS
        // ==========================================

        private int setCarParameters(
                        PreparedStatement statement,
                        Car car,
                        int startIndex) throws SQLException {

                int index = startIndex;

                statement.setString(
                                index++,
                                trimToNull(car.getStockCode()));

                statement.setString(
                                index++,
                                trimToNull(car.getName()));

                statement.setString(
                                index++,
                                trimToNull(car.getMake()));

                statement.setString(
                                index++,
                                trimToNull(car.getModel()));

                statement.setInt(
                                index++,
                                car.getManufactureYear());

                // VIN
                setNullableString(
                                statement,
                                index++,
                                normalizeUpper(car.getVin()),
                                Types.CHAR);

                // Registration number
                setNullableString(
                                statement,
                                index++,
                                normalizeUpper(car.getRegistrationNumber()),
                                Types.VARCHAR);

                // Fuel type and transmission
                statement.setString(
                                index++,
                                normalizeUpper(car.getFuelType()));

                statement.setString(
                                index++,
                                normalizeUpper(car.getTransmission()));

                // Colour
                setNullableString(
                                statement,
                                index++,
                                car.getColour(),
                                Types.VARCHAR);

                // Purchase price
                statement.setBigDecimal(
                                index++,
                                car.getPurchasePrice());

                // Acquisition date
                setNullableDate(
                                statement,
                                index++,
                                car.getAcquisitionDate());

                // Listing price
                statement.setBigDecimal(
                                index++,
                                car.getListingPrice());

                // Status
                statement.setString(
                                index++,
                                car.getStatus() == null
                                                ? null
                                                : car.getStatus().getDatabaseValue());

                // Sold date
                setNullableDate(
                                statement,
                                index++,
                                car.getSoldDate());

                // Sold price
                statement.setBigDecimal(
                                index++,
                                car.getSoldPrice());

                // Notes
                String notes = car.getNotes();

                statement.setString(
                                index++,
                                notes == null || notes.isBlank() ? null : notes);

                return index;
        }

        // ==========================================
        // HELPER 3 - ADD SEARCH CONDITION
        // ==========================================

        private void addCondition(
                        StringBuilder sql,
                        List<Object> parameters,
                        String condition,
                        Object value) {

                if (parameters.isEmpty()) {

                        sql.append(" WHERE ");

                } else {

                        sql.append(" AND ");
                }

                sql.append(condition);

                parameters.add(value);
        }

        // ==========================================
        // HELPER 4 - CHECK NON-EMPTY TEXT
        // ==========================================

        private boolean hasText(String value) {

                return value != null && !value.isBlank();
        }

        // ==========================================
        // HELPER 5 - NORMALIZE TEXT
        // ==========================================

        private String trimToNull(String value) {

                if (value == null || value.isBlank()) {
                        return null;
                }

                return value.trim();
        }

        private String normalizeUpper(String value) {

                if (value == null || value.isBlank()) {
                        return null;
                }

                return value.trim().toUpperCase(Locale.ROOT);
        }

        // ==========================================
        // HELPER 6 - SET NULLABLE STRING
        // ==========================================

        private void setNullableString(
                        PreparedStatement statement,
                        int index,
                        String value,
                        int sqlType) throws SQLException {

                if (value == null || value.isBlank()) {

                        statement.setNull(index, sqlType);

                } else {

                        statement.setString(index, value.trim());
                }
        }

        // ==========================================
        // HELPER 7 - SET NULLABLE DATE
        // ==========================================

        private void setNullableDate(
                        PreparedStatement statement,
                        int index,
                        LocalDate date) throws SQLException {

                if (date == null) {

                        statement.setNull(index, Types.DATE);

                } else {

                        statement.setDate(
                                        index,
                                        java.sql.Date.valueOf(date));
                }
        }

        private LocalDate getLocalDate(
                        ResultSet resultSet,
                        String column) throws SQLException {

                java.sql.Date date = resultSet.getDate(column);

                return date == null
                                ? null
                                : date.toLocalDate();
        }

        @Override
        public Optional<Car> findByStockCode(String stockCode) {

                String sql = "SELECT * FROM cars WHERE stock_code = ?";

                try (Connection connection = ConnectionFactory.getConnection();
                                PreparedStatement statement = connection.prepareStatement(sql)) {

                        statement.setString(1, stockCode);

                        try (ResultSet resultSet = statement.executeQuery()) {

                                if (resultSet.next()) {
                                        return Optional.of(mapRow(resultSet));
                                }

                                return Optional.empty();
                        }

                } catch (SQLException e) {
                        throw new DataAccessException(
                                        "Failed to find car using stock code: " + stockCode, e);
                }
        }
}
