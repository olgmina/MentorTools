package org.example.lectorbots.subcribe.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataManager {
    private static final String DB_URL = "jdbc:h2:mem:testdb";
    private static final String DB_USER = "test_admin";
    private static final String DB_PASSWORD = "1234567";


    private static Connection connection;



    public static void createDatabase() {
        try {
            // Check if database exists
            if (connection == null || connection.isClosed()) {
                connection = createDataSource().getConnection();
                // Create tables if database is new
                Statement stmt = connection.createStatement();
                stmt.execute("CREATE TABLE AppUser (" +
                        "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                        "telegramUserId BIGINT," +
                        "firstName VARCHAR(255)," +
                        "lastName VARCHAR(255)," +
                        "username VARCHAR(255)" +
                        ");");

                stmt.execute("CREATE TABLE Subscribe (" +
                        "subscribe_id INT PRIMARY KEY AUTO_INCREMENT," +
                        "subscribe_type VARCHAR(255)," +
                        "descriptor VARCHAR(255)," +
                        "subscribe_key VARCHAR(255)" +
                        ");");

                stmt.execute("CREATE TABLE AppUser_Subscribe (" +
                        "appUserId BIGINT," +
                        "subscribeId INT," +
                        "PRIMARY KEY (appUserId, subscribeId)," +
                        "FOREIGN KEY (appUserId) REFERENCES AppUser(id)," +
                        "FOREIGN KEY (subscribeId) REFERENCES Subscribe(subscribe_id)" +
                        ");");

                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating database: " + e.getMessage(), e);
        }
    }

    public static DataSource createDataSource() {
        try {
            Class.forName("org.h2.Driver");
            return (DataSource) DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error creating data source: " + e.getMessage(), e);
        }
    }



    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error closing database connection: " + e.getMessage(), e);
        }
    }
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = createDataSource().getConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to database: " + e.getMessage(), e);
        }
        return connection;
    }
}
