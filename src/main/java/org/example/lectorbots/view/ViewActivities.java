package org.example.lectorbots.view;

import javafx.beans.property.SimpleStringProperty;
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
        userIDColumn.setCellValueFactory(new PropertyValueFactory<Report, Long>("userID"));


        /* userIDColumn.setCellFactory(tc -> new TableCell<Report, Long>() {
            @Override
            protected void updateItem(Long value, boolean empty) {
                super.updateItem(value, empty);
                if (value == null || empty) {
                    setText("");
                } else {
                    setText(String.valueOf(value));
                }
            }
        });*/


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
        ratingColumn.setMinWidth(15);//ширина
      //  ratingColumn.setCellValueFactory(new PropertyValueFactory<Report, Integer>("rating"));

        ratingColumn.setCellFactory(new Callback<TableColumn<Report, Integer>, TableCell<Report, Integer>>() {
            @Override
            public TableCell<Report, Integer> call(TableColumn<Report, Integer> param) {
                return new TextFieldTableCell<>(toInt);
            }
        });
        ratingColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Report, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Report, Integer> t) {
                ((Report) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setRating(t.getNewValue());

            }
        });

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
       // tableView.setItems(data);

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

        //  tableView.setItems(data);
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
        tableView.setItems(data);
    }

    public BorderPane getRoot() {
        return root;
    }
}
