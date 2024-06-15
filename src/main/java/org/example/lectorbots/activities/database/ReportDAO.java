package org.example.lectorbots.activities.database;

import org.example.lectorbots.activities.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReportDAO {
    private static final String JDBC_URL = "jdbc:h2:mem:testdb"; // Замените на URL вашей БД H2
    private static final String USERNAME = "admin"; // Замените на имя пользователя вашей БД H2
    private static final String PASSWORD = "1234567"; // Замените на пароль вашей БД H2

    public ReportDAO() {
        Logger logger=LoggerFactory.getLogger(ReportDAO.class);
        // Создаем таблицу, если она еще не существует
        try {
            Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS report (ID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "telegramName VARCHAR(255), userID BIGINT,respose VARCHAR(255), idSlide INT, RATING INT, QUESTION VARCHAR(255))");
            populateDatabase(10);
        } catch (SQLException e) {
            logger.error("БД Активности не создано", e);
        }
    }

    public void create(Report report) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO report (telegramName, userID, respose, idSlide, rating, question) VALUES (?, ?, ?, ?, ?, ?)"
             )) {
            statement.setString(1, report.getTelegramName());
            statement.setLong(2, report.getUserID());
            statement.setString(3, report.getRespose().toString());
            statement.setInt(4, report.getIdSlide());
            statement.setInt(5, report.getRating());
            statement.setString(6, report.getQuestion());
            statement.executeUpdate();
        }
    }

    public Report read(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT telegramName, userID, respose, idSlide, rating, question FROM report WHERE userID = ?"
             )) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Report(
                            resultSet.getString("telegramName"),
                            resultSet.getLong("userID"),
                            resultSet.getString("respose"),
                            resultSet.getInt("idSlide"),
                            resultSet.getInt("rating"),
                            resultSet.getString("question")
                    );
                }
            }
        }
        return null;
    }

    public List<Report> getAll() throws SQLException {
        List<Report> reports = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT telegramName, userID, respose, idSlide, rating, question FROM report"
             )) {
            while (resultSet.next()) {
                reports.add(new Report(
                        resultSet.getString("telegramName"),
                        resultSet.getLong("userID"),
                        resultSet.getString("respose"),
                        resultSet.getInt("idSlide"),
                        resultSet.getInt("rating"),
                        resultSet.getString("question")
                ));
            }
        }
        return reports;
    }

    public void update(Report report) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE report SET telegramName = ?, respose = ?, idSlide = ?, rating = ?, question = ? WHERE userID = ?"
             )) {
            statement.setString(1, report.getTelegramName());

            statement.setString(3, report.getRespose().toString());
            statement.setInt(4, report.getIdSlide());
            statement.setInt(5, report.getRating());
            statement.setString(6, report.getQuestion());
            statement.setLong(2, report.getUserID());//????
            statement.executeUpdate();
        }
    }

    public void delete(long userID) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM report WHERE userID = ?")) {
            stmt.setLong(1, userID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateDatabase(int numReports) {
        Random random = new Random();
        List<String> telegramNames = List.of("user1", "user2", "user3", "user4", "user5");
        List<String> answers = List.of("Ответ 1", "Ответ 2", "Ответ 3", "Ответ 4", "Ответ 5");
        List<String> questions = List.of("Вопрос 1", "Вопрос 2", "Вопрос 3", "Вопрос 4", "Вопрос 5");

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO report (telegramName, userID, respose, idSlide, rating, question) VALUES (?, ?, ?, ?, ?, ?)")) {
            for (int i = 0; i < numReports; i++) {
                stmt.setString(1, telegramNames.get(random.nextInt(telegramNames.size())));
                stmt.setLong(2, random.nextLong()); // Генерируем случайный ID
                stmt.setString(3, answers.get(random.nextInt(answers.size())));
                stmt.setInt(4, random.nextInt(10) + 1); // Случайный ID слайда от 1 до 10
                stmt.setInt(5, random.nextInt(6)); // Случайный рейтинг от 0 до 5
                stmt.setString(6, questions.get(random.nextInt(questions.size())));
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
