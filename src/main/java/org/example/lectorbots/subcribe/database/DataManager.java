package org.example.lectorbots.subcribe.database;



import org.example.lectorbots.activities.database.ReportDAO;
import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataManager {

    private static final String DB_URL = "jdbc:h2:mem:testdb1";
    private static final String DB_USER = "test_admin";
    private static final String DB_PASSWORD = "1234567";
    private static Logger logger = LoggerFactory.getLogger(DataManager.class);

    private static  Connection connection = null;

    static void createDatabase() {

        try {
            // Check if database exists
            if (connection == null) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
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
                        "subscribe_descriptor VARCHAR(255)," +
                        "subscribe_key VARCHAR(255)" +
                        ");");

                stmt.execute("CREATE TABLE user_subscribe (" +
                        "appUserId BIGINT," +
                        "subscribeId INT," +
                        "PRIMARY KEY (appUserId, subscribeId)," +
                        "FOREIGN KEY (appUserId) REFERENCES AppUser(id)," +
                        "FOREIGN KEY (subscribeId) REFERENCES Subscribe(subscribe_id)" +
                        ");");

               // stmt.close();
                logger.debug("бАЗА ПОЛЬЗОВАТЕЛИ-ПОДПИСКИ СОЗДАНИЕ");
            }
        } catch (SQLException e) {
            logger.error("Error creating database ПОЛЬЗОВАТЕЛИ-ПОДПИСКИ: " + e.getMessage(), e);
        }
    }




    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("Error closing database connection ПОЛЬЗОВАТЕЛИ-ПОДПИСКИ:: " + e.getMessage(), e);
        }
    }


    public  static Connection getConnection() {
        if (connection == null) {
            logger.error("12345");
            createDatabase();
        }

        return connection;
    }
}
