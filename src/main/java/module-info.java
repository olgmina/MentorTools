module org.example.lectorbots {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires org.apache.poi.ooxml;
    requires java.desktop;


    opens org.example.lectorbots to javafx.fxml;
    exports org.example.lectorbots;
}