package org.example.lectorbots.view;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import org.example.lectorbots.bots.ManagerBot;
import org.example.lectorbots.bots.TelegramBotFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ViewController {

    private final TabPane tabPane = new TabPane();

    private ViewSlades viewSlades;
    private ViewVisitors viewVisitors;
    private ViewChannels viewChannels;
    private ViewActivities viewActivities;
    public ViewController() {
        ManagerBot getbot=new ManagerBot(TelegramBotFactory.BotType.SEND_IMAGE);


        //загрузка слайдов из стандартного пути
        viewSlades = new ViewSlades(getbot.bot);
        // Подписки
        viewChannels=new ViewChannels();
        // Активности
        viewActivities=new ViewActivities();
        // Участники
        viewVisitors=new ViewVisitors();
        // Создание TabPane и добавление вкладок
        Tab tab0 = new Tab("Слайды", viewSlades.viewpanel());
        Tab tab1 = new Tab("Участники", viewVisitors.getMainVBox());
        Tab tab2 = new Tab("Каналы", viewChannels.getRoot());
        Tab tab3 = new Tab("Активности", viewActivities.getRoot());

        tabPane.getTabs().addAll(tab0, tab3, tab1, tab2);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    private List<Image> loadSlides(String pathname)  {
        File folder = new File("path/to/slides"); // Замените на путь к вашей папке слайдов

        List<Image> slides = new ArrayList<>();

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".jpg")) { // Добавить поддержку других форматов
                    try {
                        slides.add(new Image(new FileInputStream(file)));
                    } catch (FileNotFoundException e) {
                        System.out.println("Ошибка чтения файла " +pathname);
                    }
                }
            }
        }
        return slides;
    }
}
