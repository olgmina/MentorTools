package org.example.lectorbots.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;


public class ViewVisitors {
    private VBox formVBox; //форма
    private TableView userTable; //таблица
    private VBox mainVBox = new VBox(10);

    public ViewVisitors() {
        FlowPane formVBox = new FlowPane();
        formVBox.setPadding(new Insets(10));

        // создание просмотра таблицы
        userTable = new TableView<>();
        userTable.setPrefHeight(392.0);
        userTable.setPrefWidth(553.0);
        // Добавляем элементы формы
        Label typeLabel = new Label("Тип подписки");
        TextField userType = new TextField();
        Label nameLabel = new Label("Имя пользователя");
        TextField userName = new TextField();
        Label surnameLabel = new Label("Фамилия пользователя");
        TextField userSurname = new TextField();
        Button saveButton = new Button("Сохранить данные");
        Button deleteButton = new Button("Удалить пользователя");

        formVBox.getChildren().addAll(
                typeLabel, userType,
                nameLabel, userName,
                surnameLabel, userSurname,
                saveButton, deleteButton
        );

        // Создаем VBox для таблицы и формы

        mainVBox.setPadding(new Insets(10));
        mainVBox.getChildren().addAll(userTable, formVBox);
    }

    public VBox getMainVBox() {
        return mainVBox;
    }
}
