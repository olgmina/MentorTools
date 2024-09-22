package org.example.lectorbots.bots;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/** предполалагалась фабрика для создания разных ботов
 * и переключения между ними для различных задач
 */
public class ManagerBot {
    private TelegramLongPollingBot bot = null;
    private String botToken="111";
    private String botUsername="1nBot";

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
