package org.example.lectorbots.view;

import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.example.lectorbots.HelloApplication;
import org.example.lectorbots.bots.SenderBot;
import org.example.lectorbots.parser.Iterator;
import org.example.lectorbots.parser.PPTXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;


import java.io.File;
import java.io.FileNotFoundException;


public class ViewSlades {

    private static final int THUMBNAIL_SIZE = 100;




    private Iterator imgIter;

    private PPTXBuilder pptxBuild;

    private BorderPane root;
    // Компоненты для управления слайдами
    private ImageView currentSlideImageView=new ImageView();

    private ImageView previousSlideImageView=new ImageView();
    private ImageView nextSlideImageView=new ImageView();
    private TextArea notesArea;
    // Компоненты для панели инструментов
    private ColorPicker colorPicker;
    private Slider lineWidthSlider;
    private Button pencilButton;
    private Button textButton;

    private SenderBot senderBot=null;

    public ViewSlades(TelegramLongPollingBot bot) {
        Logger logger = LoggerFactory.getLogger(ViewSlades.class);
        this.senderBot= (SenderBot) bot;
        try {
            pptxBuild = new PPTXBuilder("src/main/resources/slides/example.pptx");
        } catch (FileNotFoundException e) {
            logger.error("СЛАЙДЫ: Не удалось загрузить из файла", e);
        }
        imgIter = pptxBuild.getIterator();

        updateThumbnails();

    }

    private void  updateThumbnails(){
        int index=imgIter.getCurrent();
        XSLFSlide slide = imgIter.getSlide(index);

        currentSlideImageView.setImage(pptxBuild.toImage(imgIter.getSlide(imgIter.getCurrent())));
        currentSlideImageView.setFitWidth(HelloApplication.SLIDE_WIDTH);
        currentSlideImageView.setFitHeight(HelloApplication.SLIDE_HEIGHT);
        currentSlideImageView.setPreserveRatio(true);


        previousSlideImageView.setImage(pptxBuild.toImage(imgIter.getSlide(imgIter.getCurrent()-1)));
        previousSlideImageView.setFitWidth(THUMBNAIL_SIZE);
        previousSlideImageView.setFitHeight(THUMBNAIL_SIZE);
        previousSlideImageView.setPreserveRatio(true);

        nextSlideImageView.setImage(pptxBuild.toImage(imgIter.getSlide(imgIter.getCurrent()+1)));
        nextSlideImageView.setFitWidth(THUMBNAIL_SIZE);
        nextSlideImageView.setFitHeight(THUMBNAIL_SIZE);
        // nextSlideImageView.setPreserveRatio(true);

        senderBot.setImage(currentSlideImageView.snapshot(new SnapshotParameters(), null));

        pptxBuild.toText(slide);

    }

    public Pane viewpanel(){
        // Создание панели инструментов
        FlowPane toolbar = (FlowPane) createToolbar();


        // Создание текстового чата
        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);

        // Создание поля для заметок
        notesArea = new TextArea();

        // Создание панели для превью слайдов
        VBox imagePanel = new VBox(THUMBNAIL_SIZE);
        imagePanel.getChildren().add(previousSlideImageView);


        HBox thumbnailPanel = new HBox();
        Button nextButton = new Button("-->");
        Button prevButton = new Button("<--");

        nextButton.setOnAction(e -> {
            imgIter.Next();
            //( currentSlideIndex - 1 + slides.size()) % slides.size();
            updateThumbnails();
        });

        prevButton.setOnAction(e -> {
            imgIter.Prev();
            //(currentSlideIndex + 1) % slides.size();
            updateThumbnails();
        });

        thumbnailPanel.getChildren().add(prevButton);
        Button fileButton= new Button("Файл");
        fileButton.setOnAction(event -> {
            openPPTXFIle();
            updateThumbnails();
        });
        thumbnailPanel.getChildren().add(fileButton);
        thumbnailPanel.getChildren().add(nextButton);

        imagePanel.getChildren().add(thumbnailPanel);
        imagePanel.getChildren().add(nextSlideImageView);

        // Создание основного layout'а
        root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(currentSlideImageView);

        root.setLeft(chatArea);
        chatArea.setPrefColumnCount(15);
        root.setRight(imagePanel);
        root.setBottom(notesArea);

        return root;
    }

    private Pane createToolbar(){
        FlowPane toolbar = new FlowPane();

        ColorPicker colorPicker= new ColorPicker(Color.BLACK);
        toolbar.getChildren().add(colorPicker);

        Slider thicknessSlider= new Slider(1, 10, 1);
        thicknessSlider.setShowTickLabels(true);
        thicknessSlider.setShowTickMarks(true);
        thicknessSlider.setMajorTickUnit(2);
        thicknessSlider.setMinorTickCount(1);
        toolbar.getChildren().add(thicknessSlider);

        Button pencilButton= new Button("Карандаш");
        pencilButton.setOnAction(event -> {
            // Обработка события нажатия на кнопку карандаша
            // Например, изменение курсора на карандаш или включение режима рисования
        });
        toolbar.getChildren().add(pencilButton);

        Button textButton= new Button("Текст");
        textButton.setOnAction(event -> {
            // Обработка события нажатия на кнопку текста
            // Например, добавление текстового поля на изображение
        });
        toolbar.getChildren().add(textButton);

        return toolbar;
    }

    private void openPPTXFIle(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PowerPoint Presentations", "*.pptx"));
        File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (selectedFile != null) {
            try {
                pptxBuild = new PPTXBuilder(selectedFile.getAbsolutePath());
                imgIter = pptxBuild.getIterator();
                updateThumbnails();

            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Не удалось открыть файл");
                alert.setContentText("Не удалось открыть файл " + selectedFile.getName());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText("Файл не выбран");
            alert.setContentText("Пожалуйста выберите файл");
            alert.showAndWait();
        }
    }

}
