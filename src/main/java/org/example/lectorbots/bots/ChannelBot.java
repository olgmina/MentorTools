package org.example.lectorbots.bots;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ChannelBot extends TelegramLongPollingBot {
    private String botToken;
    private String botUsername;

    public ChannelBot(String botToken, String botUsername) {
        this.botToken=botToken;
        this.botUsername=botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }
}
