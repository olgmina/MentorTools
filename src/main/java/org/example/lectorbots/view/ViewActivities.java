package org.example.lectorbots.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import org.example.lectorbots.activities.Report;
import org.example.lectorbots.activities.database.ReportDAO;

import java.sql.SQLException;
import java.util.List;

public class ViewActivities {
    private TableView<Report> tableView;
    private ObservableList<Report> data;
    private ReportDAO dao;
    private BorderPane root;

    public ViewActivities() {
        dao = new ReportDAO();

        // Создаем таблицу
        tableView = new TableView<>();
        data = FXCollections.observableArrayList();
        tableView.setItems(data);

        // Добавляем колонки в таблицу
        TableColumn<Report, String> telegramNameColumn = new TableColumn<>("Telegram Name");
        telegramNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelegramName()));

        TableColumn<Report, Long> userIDColumn = new TableColumn<>("User ID");
        // userIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getUserID())));

        TableColumn<Report, String> answerColumn = new TableColumn<>("Answer");
        answerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRespose()));

        TableColumn<Report, Integer> idSlideColumn = new TableColumn<>("ID Slide");
       // idSlideColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getIdSlide())));

        TableColumn<Report, Integer> ratingColumn = new TableColumn<>("Rating");
        //ratingColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getRating())));

        TableColumn<Report, String> questionColumn = new TableColumn<>("Question");
        questionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestion()));

        tableView.getColumns().addAll(telegramNameColumn, userIDColumn, answerColumn, idSlideColumn, ratingColumn, questionColumn);

        // Создаем поля с кнопками фильтрации
        TextField telegramNameFilter = new TextField();
        Button telegramNameFilterButton = new Button("Filter");
        telegramNameFilterButton.setOnAction(e -> filterByTelegramName(telegramNameFilter.getText()));

        TextField userIDFilter = new TextField();
        Button userIDFilterButton = new Button("Filter");
        userIDFilterButton.setOnAction(e -> filterByIdSlide(userIDFilter.getText()));

        // Создаем layout для элементов
        VBox filterBox = new VBox(10);
        filterBox.setPadding(new Insets(10));
        filterBox.getChildren().addAll(
                new Label("Фильтровать по имени:"),
                new HBox(10, telegramNameFilter, telegramNameFilterButton),
                new Label("Фильтровать по слайдам:"),
                new HBox(10, userIDFilter, userIDFilterButton)
        );

        // Добавляем элементы в Scene
        root = new BorderPane();
        root.setCenter(tableView);
        root.setRight(filterBox);

        // Загружаем данные из базы данных
        loadReports();

    }

    // Загружаем все записи из базы данных
    private void loadReports() {
        try {
            data.clear();
            List<Report> reports = dao.getAll();
            data.addAll(reports);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterByTelegramName(String name) {
        data.clear();
        try {
            data.addAll(dao.getAll()); // Получаем все записи
            if (!name.isEmpty()) {
                data.removeIf(report -> !report.getTelegramName().toLowerCase().contains(name.toLowerCase()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterByIdSlide(String idSlide) {
        data.clear();
        try {
            data.addAll(dao.getAll()); // Получаем все записи
            if (!idSlide.isEmpty()) {
                try {
                    int slideId = Integer.parseInt(idSlide);
                    data.removeIf(report -> report.getIdSlide() != slideId);
                } catch (NumberFormatException e) {
                    // Некорректный ID Slide
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BorderPane getRoot() {
        return root;
    }
}
