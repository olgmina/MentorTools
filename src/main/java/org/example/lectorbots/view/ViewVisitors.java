package org.example.lectorbots.view;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import org.example.lectorbots.subcribe.database.AppUserDAO;
import org.example.lectorbots.subscribes.entries.AppUser;
import org.example.lectorbots.subscribes.entries.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** панель управления списком слушателей,
 * предварительно загруженных из файла в БД
 * и из БД в таблицу
 */

public class ViewVisitors {
    private VBox formVBox; //форма
    private TableView userTable; //таблица
    private VBox mainVBox = new VBox(10);

    ObservableList<AppUser> items = FXCollections.observableArrayList();

    AppUserDAO database=null;

    public ViewVisitors() {

        database=new AppUserDAO();
        List<AppUser> appUsers = getListOfAppUsers();
        items.addAll(appUsers );

        FlowPane formVBox = new FlowPane();
        formVBox.setPadding(new Insets(10));

        // создание просмотра таблицы
        userTable = new TableView<>();
        userTable.setPrefHeight(392.0);
        userTable.setPrefWidth(553.0);
        userTable.getItems().addAll(items);

        // Создаем колонки таблицы
        TableColumn<AppUser, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getId()).asObject());

        TableColumn<AppUser, Long> telegramUserIdColumn = new TableColumn<>("Telegram User ID");
        telegramUserIdColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getTelegramUserId()).asObject());


        TableColumn<AppUser, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));

        TableColumn<AppUser, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));

        TableColumn<AppUser, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        // Добавляем колонки в таблицу
        userTable.getColumns().addAll(idColumn, telegramUserIdColumn, firstNameColumn, lastNameColumn, usernameColumn);

        // Добавляем элементы формы
        Label typeLabel = new Label("Тип подписки");
        TextField userType = new TextField();
        Label nameLabel = new Label("Имя пользователя");
        TextField userName = new TextField();
        Label surnameLabel = new Label("Фамилия пользователя");
        TextField userSurname = new TextField();
        Button saveButton = new Button("Сохранить данные");
        saveButton.setOnAction(e->{
            AppUser user = new AppUser(userName.getText(),"", userSurname.getText());
            database.updateUser(user);
            items.addAll(database.getAllSubscribers());
        });
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
    // Заменить этот метод на свой вариант получения списка AppUser
    private List<AppUser> getListOfAppUsers() {
        if(!database.getAllSubscribers().isEmpty())
          return database.getAllSubscribers();
        else return populateAppUsers("src/main/resources/datasource/users.txt");
    }

    public  List<AppUser> populateAppUsers(String filePath) {
        List<AppUser> appUsers = new ArrayList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        long id = 1; // Счетчик для id

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            AppUser user;
                // Предполагаем, что telegramUserId не предоставлен в файле
            if(parts.length==3)  user = new AppUser( parts[0], parts[1], parts[2]);
            else if (parts.length==2)  user = new AppUser( parts[0],"", parts[1]);
                 else if (parts.length==1)  user = new AppUser( "","", parts[0]);
                      else  user = new AppUser( "","", "");
            appUsers.add(user);
            id++;
            }

        scanner.close();
        return appUsers;
    }


    public VBox getMainVBox() {
        return mainVBox;
    }
}
