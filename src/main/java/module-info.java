module org.example.lectorbots {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    requires java.desktop;
    requires java.sql;

    requires telegrambots;
    requires telegrambots.meta;
    requires org.apache.poi.ooxml;
    requires com.h2database;
    requires ch.qos.logback.classic;
    requires org.slf4j;



    opens org.example.lectorbots to javafx.fxml;
    exports org.example.lectorbots;
}