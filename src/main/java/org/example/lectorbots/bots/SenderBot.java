package org.example.lectorbots.bots;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SenderBot extends TelegramLongPollingBot {
    WritableImage image =null;

    private String botToken;
    private String botUsername;

    public SenderBot(String botToken, String botUsername) {
        this.botToken=botToken;
        this.botUsername=botUsername;
    }



    @Override
    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(image ==null) return;
        sendImage(update.getMessage().getChatId());
        System.out.println("БОТ");
    }




    public void setImage(WritableImage image){
        this.image=image;
    }

    private void sendImage(Long chatId)  {

        File file = new File("photo.png");

        FileOutputStream fileInputStream;
        try {
            fileInputStream = new FileOutputStream(file);
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", fileInputStream);

        }
        catch (FileNotFoundException e) {
            System.out.println("опять файл1");
        }
        catch (IOException e) {
            System.out.println("опять файл2");
        }

        InputFile photo = new InputFile(file);


        try {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(String.valueOf(chatId));
            sendPhoto.setPhoto(photo);
            sendPhoto.setCaption("Слайд");
            execute(sendPhoto);
            image = null;
        } catch ( TelegramApiException e) {
            e.printStackTrace();
            System.out.println("проблемы с записью изображения в канал");
        }
    }
}
