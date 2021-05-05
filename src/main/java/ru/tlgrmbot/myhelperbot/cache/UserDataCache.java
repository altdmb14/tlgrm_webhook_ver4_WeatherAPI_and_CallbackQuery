package ru.tlgrmbot.myhelperbot.cache;

import org.springframework.stereotype.Component;
import ru.tlgrmbot.myhelperbot.botapi.BotState;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory cache.
 Кэш-класс, который ХРАНИТ данные в тот момент когда проект ЗАПУЩЕН.
 После остановки проекта, все данные ОБНУЛЯТСЯ.
 */

@Component
public class UserDataCache {
    private Map<Integer, BotState> usersBotStates = new HashMap<>();

    public void setUsersCurrentBotState(int userId, BotState botState) {
        usersBotStates.put(userId, botState);
    }

    public BotState getUsersCurrentBotState(int userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.WEATHER_HELPER;
        }
        else if (botState == BotState.START_HELPER) {
            botState = BotState.WEATHER_HELPER;
        }

        return botState;
    }

}