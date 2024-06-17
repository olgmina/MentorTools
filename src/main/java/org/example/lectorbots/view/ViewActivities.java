package org.example.lectorbots.view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.example.lectorbots.activities.Report;
import org.example.lectorbots.activities.database.ReportDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ViewActivities {
    private static final Logger log = LoggerFactory.getLogger(ViewActivities.class);
    private TableView<Report> tableView;
    private ObservableList<Report> data;
    private ReportDAO dao;
    private BorderPane root;
    private StringConverter<Integer> toInt=new IntegerStringConverter();


    public ViewActivities(ReportDAO db) {
        this.dao=db;

        // Создаем таблицу
        tableView = new TableView<>();
        data = FXCollections.observableArrayList();
        tableView.setItems(data);

        // Добавляем колонки в таблицу
        TableColumn<Report, String> telegramNameColumn = new TableColumn<>("Telegram Name");
        telegramNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelegramName()));

        TableColumn<Report, Long> userIDColumn = new TableColumn<>("User ID");
        userIDColumn.setCellValueFactory(cellData->cellData.getValue().userIDProperty().asObject());

        TableColumn<Report, String> answerColumn = new TableColumn<>("Answer");
        answerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRespose()));

        TableColumn<Report, Integer> idSlideColumn = new TableColumn<>("ID Slide");
        idSlideColumn.setCellValueFactory(cellData -> cellData.getValue().idSlideProperty().asObject());

        TableColumn<Report, Integer> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setMinWidth(15);//ширина
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().getratingProperty().asObject());

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

        TextField answerFilter = new TextField();
        Button answerButton = new Button("Filter");
        answerButton.setOnAction(e -> filterByAnswer(answerFilter.getText()));

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
                new Label("Фильтровать по ответам:"),
                new HBox(10, answerFilter, answerButton),
                statisticaButton,
                loadButton
                
        );
        tableView.setOnMouseClicked(mouseEvent -> {
            int i=tableView.getSelectionModel().getSelectedIndex();
            answerFilter.setText(data.get(i).getRespose());
            telegramNameFilter.setText(data.get(i).getTelegramName());
            userIDFilter.setText(""+data.get(i).getuserID());
        });
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
        data.addAll(reports);

        // Получаем все записи
        if (!searchValue.isEmpty()) {
            for(Report a:reports)
                if(!a.getTelegramName().equals(searchValue)) data.remove(a);

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
                    for(Report a:reports)
                        if(a.getIdSlide()!=slideId) data.remove(a);
                } catch (NumberFormatException e) {
                    // Некорректный ID Slide
                }
            }

    }

    private void filterByAnswer(String answer) {
        data.clear();
        List<Report> reports = dao.getAll();
        data.addAll(reports);
        // Получаем все записи
        if (!answer.isEmpty()) {
            for(Report a:reports)
                if(!a.getTelegramName().equals(answer)) data.remove(a);

        }

    }

    public BorderPane getRoot() {
        return root;
    }
}
