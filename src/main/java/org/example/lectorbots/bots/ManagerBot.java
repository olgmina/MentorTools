package org.example.lectorbots.bots;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class ManagerBot {
    private TelegramLongPollingBot bot = null;
    private String botToken="6165348160:AAHXc_ZzpxyAPLfgH9gfZnKS8BVlYihJlaU";
    private String botUsername="LectionSWQuestionBot";

    public ManagerBot()  {
        bot=new AdminBot(botToken, botUsername);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            System.out.println("БОТЫ не произведены     - тип:");
        }
    }

    public TelegramLongPollingBot getBot() {
        return bot;
    }
}
