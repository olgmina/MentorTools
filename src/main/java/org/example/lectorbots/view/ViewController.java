package org.example.lectorbots.view;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.example.lectorbots.activities.database.ReportDAO;
import org.example.lectorbots.bots.AdminBot;
import org.example.lectorbots.bots.ManagerBot;

public class ViewController {

    private final TabPane tabPane = new TabPane();

    private ViewSlades viewSlades;
    private ViewVisitors viewVisitors;
    private ViewChannels viewChannels;
    private ViewActivities viewActivities;

    ReportDAO database;
    public ViewController() {
        ManagerBot managerBot =new ManagerBot();
        AdminBot bot= (AdminBot) managerBot.getBot();

        //загрузка слайдов из стандартного пути
        viewSlades = new ViewSlades(bot);
        // Подписки
        viewChannels=new ViewChannels();
        // Активности
        database = new ReportDAO();
        viewActivities=new ViewActivities(database);
        bot.setDataBase(database);
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


}
