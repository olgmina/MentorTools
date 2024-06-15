package org.example.lectorbots.bots;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class TelegramBotFactory {
    private String botToken="6165348160:AAHXc_ZzpxyAPLfgH9gfZnKS8BVlYihJlaU";
    private String botUsername="LectionSWQuestionBot";

    public TelegramBotFactory(String botToken, String botUsername) {
        this.botToken = botToken;
        this.botUsername = botUsername;
    }

    public TelegramBotFactory() {

    }

    public TelegramLongPollingBot createBot(BotType type) {
        switch (type) {
            case SEND_IMAGE:
                return new SenderBot(botToken, botUsername);
            case COLLECT_MESSAGES:
                return new AdminBot(botToken, botUsername);
            case POLL_CHANNEL:
                return new ChannelBot(botToken, botUsername);
            default:
                throw new IllegalArgumentException("Invalid bot type: " + type);
        }
    }

    public enum BotType {
        SEND_IMAGE,
        COLLECT_MESSAGES,
        POLL_CHANNEL
    }
}
