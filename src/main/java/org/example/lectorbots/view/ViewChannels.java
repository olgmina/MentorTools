package org.example.lectorbots.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.lectorbots.subscribes.entries.Subscribe;
import org.example.lectorbots.subcribe.database.*;
import static org.apache.poi.poifs.crypt.CryptoFunctions.generateKey;

public class ViewChannels {
    private TableView subscribeTable=new TableView<>();
    private TableColumn<Subscribe, Integer> subscribeIdColumn;
    private TableColumn<Subscribe, String> subscribeTypeColumn;
    private TableColumn<Subscribe, String> descriptionColumn;
    
    private ObservableList<Subscribe> subscribeData = FXCollections.observableArrayList();
    private SubscribeDAO subscribeDAO = new SubscribeDAO();

    private TextField subscribeTypeField;
    private TextField descriptionField;
    private HBox controlsBox;

    private VBox root; //основная панель
    public ViewChannels() {
        subscribeIdColumn = new TableColumn<>("ID");
        subscribeIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSubscribe_id()).asObject());

        subscribeTypeColumn = new TableColumn<>("Тип подписки");
        subscribeTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubscribe_type()));

        descriptionColumn = new TableColumn<>("Описание");
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        subscribeTable.getColumns().addAll(subscribeIdColumn, subscribeTypeColumn, descriptionColumn);

        // данные?
        subscribeData.addAll(subscribeDAO.getAllSubscribe());
        subscribeTable.getItems().addAll(subscribeData);

        // Добавление элементов управления
        subscribeTypeField = new TextField();
        descriptionField = new TextField();

        Button addButton = new Button("Добавить");
        addButton.setOnAction(event -> addSubscribe());

        Button deleteButton = new Button("Удалить");
        deleteButton.setOnAction(event -> deleteSubscribe());

        Button editButton = new Button("Изменить");
        editButton.setOnAction(event -> editSubscribe());

        Button generateKeyButton = new Button("Сгенерировать ключ");
        generateKeyButton.setOnAction(event -> generateKey());

        // Размещение элементов управления
        controlsBox = new HBox(10);
        controlsBox.getChildren().addAll(subscribeTypeField, descriptionField, addButton, deleteButton, editButton, generateKeyButton);
        controlsBox.setAlignment(Pos.CENTER);

        root = new VBox(10);
        root.getChildren().addAll(subscribeTable, controlsBox);
        root.setPadding(new Insets(10));
    }

    private void generateKey() {
    }

    private void editSubscribe() {
        
    }

    // Метод для добавления новой подписки
    private void addSubscribe() {

        String subscribeType = subscribeTypeField.getText();
        String description = descriptionField.getText();


        if (!subscribeType.isEmpty() && !description.isEmpty()) {
            Subscribe newSubscribe = new Subscribe(subscribeType, description);
            subscribeData.add(newSubscribe);
            subscribeTable.getItems().add(newSubscribe);

            // Очистка полей ввода
            subscribeTypeField.clear();
            descriptionField.clear();
        } else {
            // Вывод сообщения об ошибке, если поля ввода пусты
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, заполните все поля.");
            alert.showAndWait();
        }
    }

    // Метод для удаления выбранной подписки
    private void deleteSubscribe() {
    }

    public VBox getRoot() {
        return root;
    }
}
