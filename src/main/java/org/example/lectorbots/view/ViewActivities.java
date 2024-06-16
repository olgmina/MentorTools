package org.example.lectorbots.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import org.example.lectorbots.activities.Report;
import org.example.lectorbots.activities.database.ReportDAO;
import org.example.lectorbots.bots.ChannelBot;
import org.example.lectorbots.bots.SenderBot;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.sql.SQLException;
import java.util.List;

public class ViewActivities {
    private TableView<Report> tableView;
    private ObservableList<Report> data;
    private ReportDAO dao;
    private BorderPane root;

    private ChannelBot bot=null;

    public ViewActivities(TelegramLongPollingBot bot) {
        dao = new ReportDAO();
        this.bot= (ChannelBot) bot;
        ((ChannelBot) bot).setDatabase(dao);
        // Создаем таблицу
        tableView = new TableView<>();
        data = FXCollections.observableArrayList();
        tableView.setItems(data);

        // Добавляем колонки в таблицу
        TableColumn<Report, String> telegramNameColumn = new TableColumn<>("Telegram Name");
        telegramNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelegramName()));

        TableColumn<Report, Long> userIDColumn = new TableColumn<>("User ID");
        userIDColumn.setCellValueFactory(new PropertyValueFactory<Report, Long>("userID"));


        userIDColumn.setCellFactory(tc -> new TableCell<Report, Long>() {
            @Override
            protected void updateItem(Long value, boolean empty) {
                super.updateItem(value, empty);
                if (value == null || empty) {
                    setText("");
                } else {
                    setText(String.valueOf(value));
                }
            }
        });


        TableColumn<Report, String> answerColumn = new TableColumn<>("Answer");
        answerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRespose()));

        TableColumn<Report, Integer> idSlideColumn = new TableColumn<>("ID Slide");
        /*idSlideColumn.setCellValueFactory(new Callback<CellDataFeatures<Report, String>, ObservableValue<String>>() {
           public ObservableValue<String> call(CellDataFeatures<Report, String> p) {
               // p.getValue() returns the Report instance for a particular TableView row
               return p.getValue().firstNameProperty();
           }
       });*/
        TableColumn<Report, Integer> ratingColumn = new TableColumn<>("Rating");
     //  ratingColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRating()));

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

        Button loadButton = new Button("Загрузить все");
        loadButton.setOnAction(e->loadReports());

        Button statisticaButton = new Button("Статистика");
        statisticaButton.setOnAction(e->infoSlide());

        // Создаем layout для элементов
        VBox filterBox = new VBox(10);
        filterBox.setPadding(new Insets(10));
        filterBox.getChildren().addAll(
                new Label("Фильтровать по имени:"),
                new HBox(10, telegramNameFilter, telegramNameFilterButton),
                new Label("Фильтровать по слайдам:"),
                new HBox(10, userIDFilter, userIDFilterButton),
                statisticaButton,
                loadButton
                
        );

        // Добавляем элементы в Scene
        root = new BorderPane();
        root.setCenter(tableView);
        root.setRight(filterBox);

        // Загружаем данные из базы данных
        loadReports();

    }

    private void infoSlide() {
    }

    // Загружаем все записи из базы данных
    private void loadReports() {

            data.clear();
            List<Report> reports = dao.getAll();
            data.addAll(reports);

    }

    // по слушателю выбрать вопросы
    private void filterByTelegramName(String searchValue) {
        data.clear();
        List<Report> reports = dao.getAll();
        data.addAll(reports); // Получаем все записи
        if (!searchValue.isEmpty()) {
            data.filtered(report -> report.getTelegramName().equals(searchValue));
        }
    }

    // по слайду выбрать вопросы
    private void filterByIdSlide(String idSlide) {
        data.clear();
        List<Report> reports = dao.getAll();
        data.addAll(reports);
            if (!idSlide.isEmpty()) {
                try {
                    int slideId = Integer.parseInt(idSlide);
                    data.filtered(report -> report.getIdSlide() != slideId);
                } catch (NumberFormatException e) {
                    // Некорректный ID Slide
                }
            }

    }

    public BorderPane getRoot() {
        return root;
    }
}
