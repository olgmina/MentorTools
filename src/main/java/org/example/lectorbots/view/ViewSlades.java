package org.example.lectorbots.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.example.lectorbots.HelloApplication;
import org.example.lectorbots.activities.Report;
import org.example.lectorbots.activities.database.ReportDAO;
import org.example.lectorbots.bots.AdminBot;
import org.example.lectorbots.parser.Iterator;
import org.example.lectorbots.parser.PPTXBuilder;
import org.example.lectorbots.view.pens.MPlus;
import org.example.lectorbots.view.pens.MPoint;
import org.example.lectorbots.view.pens.MRectangle;
import org.example.lectorbots.view.pens.MRing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class ViewSlades {

    private static final int THUMBNAIL_SIZE = 100;



    private MPoint curpen=null;
    private Iterator imgIter=null;
    private PPTXBuilder pptxBuild=null;

    private BorderPane root;
    // Компоненты для управления слайдами
    private Canvas currentSlideView=new Canvas(600, 400);


    // панель навигации по слайдам
    private ImageView previousSlideImageView=new ImageView();
    private ImageView nextSlideImageView=new ImageView();
    //заметки к слайдам
    private TextArea notesArea;
    // Компоненты для панели инструментов
    private ColorPicker colorPicker;
    private Slider lineWidthSlider;
    private Button pencilButton;
    private Button textButton;
    // чат
    TextArea chatArea;

    private AdminBot senderBot=null;
    Logger logger = LoggerFactory.getLogger(ViewSlades.class);

    public ViewSlades(TelegramLongPollingBot bot) {
        this.senderBot= (AdminBot) bot;

        // Создание панели инструментов
        FlowPane toolbar = (FlowPane) createToolbar();

        // Создание текстового чата
        chatArea = new TextArea();
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
            chooserFile();
        });
        thumbnailPanel.getChildren().add(fileButton);
        thumbnailPanel.getChildren().add(nextButton);

        imagePanel.getChildren().add(thumbnailPanel);
        imagePanel.getChildren().add(nextSlideImageView);

        //Панель с изображением для рисования
        currentSlideView.setOnMousePressed(e->{

             if(curpen!=null){
                currentSlideView.setCursor(getPenView());
                return;
            }
        });

        currentSlideView.setOnMouseDragged(e->{
            if(curpen!=null) {
                curpen.setX(e.getX());
                curpen.setY(e.getY());
                curpen.draw(currentSlideView.getGraphicsContext2D());
            }

        });

        currentSlideView.setOnMouseReleased(e->{
            currentSlideView.setCursor(Cursor.DEFAULT);
            if(curpen!=null) {
                curpen = null;

            }

        });



        // Создание основного layout'а
        root = new BorderPane();

        root.setTop(toolbar);

        root.setAlignment(currentSlideView, Pos.TOP_LEFT);
        root.setMargin(currentSlideView, new Insets(5,5,5,5));
        root.setCenter(currentSlideView);

        root.setAlignment(chatArea, Pos.TOP_LEFT);
        root.setMargin(chatArea, new Insets(5,5,5,5));
        root.setLeft(chatArea);
        chatArea.setPrefColumnCount(15);

        root.setAlignment(imagePanel, Pos.TOP_LEFT);
        root.setMargin(imagePanel, new Insets(5,5,5,5));
        root.setRight(imagePanel);

        root.setBottom(notesArea);



        try {
            openPPTXFIle("src/main/resources/slides/example.pptx");
        } catch (FileNotFoundException e) {
            logger.error("СЛАЙДЫ: Не удалось загрузить из файла", e);
        }

    }

    private void updateChat(){
        ReportDAO dao=senderBot.getDatabase();
        if(dao!=null){
        List<Report> reports = dao.getAll();
        List<Report> data =new ArrayList<>();
        data.addAll(reports);
        String text="";
        // Получаем все записи
        for(Report a:reports)
           if(a.getIdSlide()!=imgIter.getCurrent()) data.remove(a);
        for(Report a:data)
            text+=a.getQuestion()+"\n";
        chatArea.setText(text);}
    }



    private void  updateThumbnails(){
        if(imgIter==null) {
            notesArea.setText("Не загружены слайды");
            return;
        }

        int index=imgIter.getCurrent();
        XSLFSlide slide = imgIter.getSlide(index);

        currentSlideView.getGraphicsContext2D().clearRect(0,0, currentSlideView.getWidth(), currentSlideView.getHeight());
        currentSlideView.getGraphicsContext2D().drawImage(pptxBuild.toImage(slide),1,1);


        previousSlideImageView.setImage(pptxBuild.toImage(imgIter.getSlide(imgIter.getCurrent()-1)));
        previousSlideImageView.setFitWidth(THUMBNAIL_SIZE);
        previousSlideImageView.setFitHeight(THUMBNAIL_SIZE);
        previousSlideImageView.setPreserveRatio(true);

        nextSlideImageView.setImage(pptxBuild.toImage(imgIter.getSlide(imgIter.getCurrent()+1)));
        nextSlideImageView.setFitWidth(THUMBNAIL_SIZE);
        nextSlideImageView.setFitHeight(THUMBNAIL_SIZE);
        // nextSlideImageView.setPreserveRatio(true);

        senderBot.setImage(currentSlideView.snapshot(new SnapshotParameters(), null));//иммено изображение, так как слайд может быть изменен
        senderBot.setCaption(pptxBuild.toCaption(slide));

        notesArea.setText( pptxBuild.toText(slide));

        updateChat();
    }

    public Pane viewpanel(){
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

        ListView<MPoint> pencilButton= new ListView<>();
        pencilButton.setMaxHeight(20);
        ObservableList<MPoint> pens=  FXCollections.observableArrayList();
        pens.add(new MRectangle(1,1, Color.BLACK,3,5));
        pens.add(new MRing(1,1, Color.BLACK,3));
        pens.add(new MPlus(1,1, Color.BLACK,1,1,2,2));
        pencilButton.setItems(pens);
        pencilButton.setOnEditCommit(e->{
            curpen=e.getNewValue();
        });
        curpen=pens.get(0);
        toolbar.getChildren().add(pencilButton);

        Button textButton= new Button("Текст");
        textButton.setOnAction(event -> {
            // Обработка события нажатия на кнопку текста
            // Например, добавление текстового поля на изображение
        });
        toolbar.getChildren().add(textButton);

        return toolbar;
    }

    private void chooserFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PowerPoint Presentations", "*.pptx"));
        File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (selectedFile != null) {
            try {
                openPPTXFIle (selectedFile.getAbsolutePath());


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

    private void openPPTXFIle(String file) throws FileNotFoundException {
        pptxBuild = new PPTXBuilder(file);
        imgIter = pptxBuild.getIterator();
        senderBot.setCaption(pptxBuild.toCaption(imgIter.getSlide(imgIter.getCurrent())));
        updateThumbnails();
    }

    public ImageCursor getPenView(){
        if (curpen == null) return (ImageCursor) Cursor.DEFAULT;
        Canvas canv = new Canvas();
        canv.setWidth(10);
        canv.setHeight(10);
        curpen.setX(1);
        curpen.setY(1);
        curpen.draw(canv.getGraphicsContext2D());
        ImageCursor cursor = new ImageCursor(canv.snapshot(null, null));
        return cursor;
    }
}
