package org.example.lectorbots;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.lectorbots.view.ViewSlades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    public static final int SLIDE_WIDTH = 800;
    public static final int SLIDE_HEIGHT = 600;
    @Override
    public void start(Stage stage) throws IOException {


        ViewSlades root = new ViewSlades(loadSlides("path/to/slides"),0);

        Scene scene = new Scene(root.viewpanel(), SLIDE_WIDTH + 200, SLIDE_HEIGHT + 100);
        stage.setTitle("Presentation Panel");
        stage.setScene(scene);
        stage.show();
    }

    private List<Image> loadSlides(String pathname) throws FileNotFoundException {
        File folder = new File("path/to/slides"); // Замените на путь к вашей папке слайдов

        List<Image> slides = new ArrayList<>();

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".jpg")) { // Добавить поддержку других форматов
                    slides.add(new Image(new FileInputStream(file)));
                }
            }
        }
        return slides;
    }

    public static void main(String[] args) {
        launch();
    }
}