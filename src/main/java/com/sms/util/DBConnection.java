package com.sms.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Single place responsible for opening JDBC connections to MySQL.
 * Keeping this logic in one class (instead of duplicating it in every
 * DAO method) is what "modular design" refers to in the project summary.
 *
 * Credentials are read from src/main/resources/db.properties instead of
 * being hardcoded, so the same code can run against a different
 * database just by editing a config file.
 */
public final class DBConnection {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("db.properties not found on the classpath.");
            }
            PROPS.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load db.properties", e);
        }
    }

    private DBConnection() {
        // utility class - no instances
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found on classpath.", e);
        }

        return DriverManager.getConnection(
                PROPS.getProperty("db.url"),
                PROPS.getProperty("db.username"),
                PROPS.getProperty("db.password")
        );
    }
}
