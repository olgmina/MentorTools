package org.example.lectorbots.bots;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import org.example.lectorbots.activities.Report;
import org.example.lectorbots.activities.database.ReportDAO;

import org.example.lectorbots.bots.handlers.KeyboardBuilder;
import org.example.lectorbots.subcribe.database.AppUserDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminBot extends TelegramLongPollingBot {
    private String botToken;
    private String botUsername;

    private KeyboardBuilder keyboardBuilder;

    private Logger logger= LoggerFactory.getLogger(TelegramLongPollingBot.class);

    private AppUserDAO userDAO ;
    private ReportDAO database;

    private WritableImage image =null;
    private int index;
    private String Caption="Нет лекции";

    public AdminBot(String botToken, String botUsername) {
        this.botToken=botToken;
        this.botUsername=botUsername;


        index=0;
    }

    public void setDataBase(ReportDAO reportDAO) {
        this.database = reportDAO;
    }

    public ReportDAO getDatabase() {
        return database;
    }

    public void setImage(WritableImage image){
        this.image=image;
        index++;
    }



    public void setCaption(String caption) {
        Caption = caption;
        index=0;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
    //    if (update.hasMessage() && update.getMessage().isReply()) {



                // Получаем имя пользователя
                String username = message.getFrom().getUserName();
                // Получаем текст сообщения
                String response = message.getText();
                // Получаем заголовок
                String captionMessage = message.getCaption();



                if(response.startsWith("/")) {
                    sendImage(message.getChatId()); //отправка изображения
                    return;
                }

                if (database == null) {
                    logger.info("База не подключена");
                    sendMessage.setText("Сообщение НЕ может быть сохранено!");
                }
                else   {
            logger.info(" БОТ интерпретирует сообщение как ответ");
            if (response.length() == 1 && response.matches("[1-5]")) {

                int rating = Integer.parseInt(response);
                Report report = new Report(username, 1, "ответ", index, rating, "");
                try {
                    database.create(report);
                } catch (SQLException e) {
                    logger.error("БД ОТВЕТОВ не работает " + e);
                }

                sendMessage.setText("Оценка: '" + rating + "'\nУспешно сохранена");
            } else if (response.endsWith("?")) {

                Report report = new Report(username, 1, "ответ", index, -1, response);
                try {
                    database.create(report);
                } catch (SQLException e) {
                    logger.error("БД ОТВЕТОВ не работает " + e);
                }

                sendMessage.setText("Вопрос: '" + response + "'\nУспешно сохранён");
            } else if(response.startsWith("_")){
                Report report = new Report(username, 1, response, 789, -1, "");
                try {
                    database.create(report);
                } catch (SQLException e) {
                    logger.error("БД ОТВЕТОВ не работает " + e);
                }

                sendMessage.setText("Ответ: '" + response + "'\nУспешно сохранён");
               }
            else  sendMessage.setText("Для получения слайда - '//' /n для вопроса - '?' в конце текста  ");
        }
        sender(sendMessage);

    }


    private int extractNumber(String text) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        } else {
            return 0; // Возвращаем 0, если число не найдено
        }
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
            sendPhoto.setCaption(this.Caption);
            execute(sendPhoto);

        } catch ( TelegramApiException e) {
            e.printStackTrace();
            System.out.println("проблемы с записью изображения в канал");
        }
    }


        private void welcomeMessage(Message message,SendMessage sendMessage){
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Добро пожаловать!");
        long telegramUserId = message.getFrom().getId();
        if (userDAO.isUserRegistered(telegramUserId)) {
            sendMessage.setReplyMarkup(keyboardBuilder.getMainUserKeyboard());


        }else{
            sendMessage.setReplyMarkup(keyboardBuilder.getMainNotUserKeyboard());
        }
        sender(sendMessage);
    }

    private void sender (SendMessage sendMessage){
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {

        return botToken;
    }
}

