package org.example.lectorbots.bots;

import org.example.lectorbots.activities.Report;
import org.example.lectorbots.activities.database.ReportDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChannelBot extends TelegramLongPollingBot {

    private String botToken;
    private String botUsername;
    private Logger logger= LoggerFactory.getLogger(TelegramLongPollingBot.class);
    private ReportDAO database=null;

    public ChannelBot(String botToken, String botUsername) {
        this.botToken=botToken;
        this.botUsername=botUsername;
        System.out.println("БОТ ОБратной связи");
    }

    public void setDatabase(ReportDAO database) {
        this.database = database;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                // Получаем имя пользователя
                String username = message.getFrom().getUserName();
                // Получаем текст сообщения
                String response = message.getText();
                // Получаем заголовок
                String caption = message.getReplyToMessage().getCaption();

                SendMessage sendMessage = new SendMessage();

                if (database == null) {
                    logger.info("База не подключена");
                    sendMessage.setText("Сообщение НЕ охранено!");
                    return;
                }

                if (response.length() == 1 && response.matches("[1-5]")) {
                    int rating = Integer.parseInt(response);
                    Report report = new Report(username, 1, "ответ", extractNumber(caption), rating, "");
                    try {
                        database.create(report);
                    } catch (SQLException e) {
                        logger.error("БД ОТВЕТОВ не работает " + e);
                    }

                    sendMessage.setChatId(message.getChatId().toString());
                    sendMessage.setText("Оценка: '" + rating + "'\nУспешно сохранена");
                } else if (response.endsWith("?")) {
                    Report report = new Report(username, 1, "ответ", extractNumber(caption), -1, response);
                    try {
                        database.create(report);
                    } catch (SQLException e) {
                        logger.error("БД ОТВЕТОВ не работает " + e);
                    }
                    sendMessage.setChatId(message.getChatId().toString());
                    sendMessage.setText("Вопрос: '" + response + "'\nУспешно сохранён");
                } else {
                    Report report = new Report(username, 1, response, extractNumber(caption), -1, "");
                    try {
                        database.create(report);
                    } catch (SQLException e) {
                        logger.error("БД ОТВЕТОВ не работает " + e);
                    }
                    sendMessage.setChatId(message.getChatId().toString());
                    sendMessage.setText("Ответ: '" + response + "'\nУспешно сохранён");
                }


                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    logger.error("БОТ ОБРАТНОЙ СВЯЗИ сообщение не отправлено" +e);
                }

            }

        }
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
    @Override
    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }
}
