package org.example.lectorbots;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.lectorbots.view.ViewController;
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


        ViewController viewController = new ViewController();

        Scene scene = new Scene(viewController.getTabPane(), SLIDE_WIDTH + 200, SLIDE_HEIGHT + 100);
        stage.setTitle("Presentation Panel");
        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}