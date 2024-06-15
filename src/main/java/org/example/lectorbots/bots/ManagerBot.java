package org.example.lectorbots.bots;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class ManagerBot {
    public TelegramLongPollingBot bot = null;

    public ManagerBot(TelegramBotFactory.BotType type)  {
        TelegramBotFactory factory = new TelegramBotFactory();
        bot= factory.createBot(type);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            System.out.println("БОТЫ не произведены     - тип:"+ type);
        }
    }
}
