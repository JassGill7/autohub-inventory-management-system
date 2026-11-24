
package com.autohub.db;

import java.io.FileInputStream;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Properties;

public class ConnectionFactory {

    private static final Properties properties = new Properties();

    static {

        try (FileInputStream input =
                     new FileInputStream("db.properties")) {

            properties.load(input);

        } catch (IOException e) {

            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection()
            throws SQLException {

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        return DriverManager.getConnection(
                url,
                username,
                password
        );
    }
}
