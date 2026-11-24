
package com.autohub;

import com.autohub.db.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        System.out.println("==============================");
        System.out.println("    AUTOHUB INVENTORY SYSTEM");
        System.out.println("==============================");

        String query = "SELECT COUNT(*) FROM cars";

        try (Connection connection =
                     ConnectionFactory.getConnection()) {

            System.out.println("\nDatabase connection successful!");

            System.out.println(
                    "Connected Database: " +
                    connection.getCatalog()
            );

            try (PreparedStatement statement =
                         connection.prepareStatement(query);

                 ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    long count = resultSet.getLong(1);

                    System.out.println(
                            "Cars table accessible!"
                    );

                    System.out.println(
                            "Existing Car Records: " + count
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println("\nDatabase connection failed!");

            System.out.println("Error: " + e.getMessage());

            e.printStackTrace();
        }
    }
}
